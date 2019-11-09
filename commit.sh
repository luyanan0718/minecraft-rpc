echo  '开始提交文件'
mv  .git_bak    .git &&  git add . && git commit -m "添加笔记" &&  git  push    &&  mv  .git  .git_bak    && echo  '提交成功'