#1 knife4j包报错  Error:java: error in opening zip file 问题
答： 确实是jar文件出现问题，使用jd-gui.exe打开文件报错就能验证文件出错，解决这个问题，就是清空所有knife4j所有jar包，
    并从新 从maven仓库下载新文件。