package com.minecraft.rpc;

import com.minecraft.rpc.annotation.EnableRpc;
import com.minecraft.rpc.annotation.RpcClient;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author luyanan
 * @since 2019/11/9
 * <p> 客户端的接口的注册</p>
 **/
public class RpcClientRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {


    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    private Environment environment;


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {


        //  将带有RpcClient 注解的接口进行注册到IOC 中
        registryRpcClient(annotationMetadata, beanDefinitionRegistry);

    }

    private void registryRpcClient(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        Set<String> basePackages;
        Map<String, Object> attrs = annotationMetadata.getAnnotationAttributes(EnableRpc.class.getName());
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RpcClient.class);
        final Class<?>[] clients = attrs == null ? null : (Class<?>[]) attrs.get("clients");

        if (clients == null || clients.length == 0) {
            scanner.addIncludeFilter(annotationTypeFilter);
            basePackages = getBasePackages(annotationMetadata);
        } else {
            final Set<String> clientClasses = new HashSet<>();
            basePackages = new HashSet<>();
            for (Class<?> client : clients) {
                basePackages.add(ClassUtils.getPackageName(client));
                clientClasses.add(client.getCanonicalName());
            }


            AbstractClassTestingTypeFilter filter = new AbstractClassTestingTypeFilter() {
                @Override
                protected boolean match(ClassMetadata classMetadata) {
                    String cleaned = classMetadata.getClassName().replaceAll("\\$", ".");
                    return clientClasses.contains(cleaned);
                }
            };
            scanner.addIncludeFilter(new AllTypeFilter(Arrays.asList(filter, annotationTypeFilter)));

        }


        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {

                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;

                    AnnotationMetadata metadata = beanDefinition.getMetadata();
//                    Assert.isTrue(annotationMetadata.isInterface(),
//                            "@RpcClient can only be specified on an interface");

                    Map<String, Object> attributes = metadata.getAnnotationAttributes(RpcClient.class.getCanonicalName());
                    registerRpcClient(beanDefinitionRegistry, metadata, attributes);
                }
            }
        }

    }

    private void registerRpcClient(BeanDefinitionRegistry beanDefinitionRegistry, AnnotationMetadata metadata, Map<String, Object> attributes) {

        String className = metadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(RpcProxyClientFactoryBean.class);

        String version = (String) attributes.get("version");
        definition.addPropertyValue("version", version);
        definition.addPropertyValue("interfaceClazz", className);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        boolean primary = (boolean) attributes.get("primary");

        beanDefinition.setPrimary(primary);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, beanDefinitionRegistry);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {

        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata()
                            .getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition
                            .getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(
                                    beanDefinition.getMetadata().getClassName(),
                                    RpcClientRegistry.this.classLoader);
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: "
                                            + beanDefinition.getMetadata().getClassName(),
                                    ex);

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }


    protected Set<String> getBasePackages(AnnotationMetadata annotationMetadata) {


        Map<String, Object> attributes =
                annotationMetadata.getAnnotationAttributes(EnableRpc.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(annotationMetadata.getClassName()));
        }
        return basePackages;
    }


    private static class AllTypeFilter implements TypeFilter {

        private final List<TypeFilter> delegates;


        public AllTypeFilter(List<TypeFilter> delegates) {
            Assert.notNull(delegates);
            this.delegates = delegates;
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {


            for (TypeFilter delegate : this.delegates) {
                if (!delegate.match(metadataReader, metadataReaderFactory)) {
                    return false;
                }
            }
            return true;
        }
    }
}
