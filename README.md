# 智悲研藏（Smart Library）

这是一款具有自维护内容、强大搜索功能、数据本地化、数据可分享等特性的知识库系统！

# 用途

这是我的用途：
![Untitled-220207](https://user-images.githubusercontent.com/10892032/152894046-a78eab16-0332-44db-9a80-cb7601bbe5db.png)

主要用于对个人收集的大量资料建立索引，并提供基于目录名、文件名、内容标题的快捷检索，以及基于文本内容的全文检索。

同时也支持非“自维护内容”的任意文件资料。

除了个人用于归档数据检索外，也可将收录的内容分享给他人。比如收录众多文集，并将数据打包分享。

# 用法

本程序提供一个树状结构的“资源管理器”用于管理所有资源，实际数据均存储在本地磁盘中，所有针对数据的功能操作可在资源管理器中通过右键菜单完成。（参考功能说明部分）

## 分享的数据

本系统主程序默认不带数据，为便于入手故建议先使用他人分享的数据包。

分享的数据包文件后缀名为“*.smartlib”，在资源管理器中右键并使用“导入数据包”即可。

## 自维护内容的数据

这是推荐的方式。

本系统提供一种“图文”类型的数据格式，支持丰富的排版样式，支持内嵌图片等。

使用此方式管理数据的优点是，可以灵活控制数据中可检索的内容，比如凡是标记为“标题1-6级别”的内容均可被快捷检索；使用“分页”功能对长文进行细分更便于全文检索；

可通过“添加 图文”（快捷键“Ctrl/Command + N”）增加数据，编辑内容，并设置一些适当的“元数据”，保存。之后即可正常使用查看、快捷检索、全文搜索等功能。

可通过“添加 目录”（快捷键“Ctrl/Command + Shift + N”）增加目录，以便于分门别类。（虽然理论上将所有数据放到一个目录中也不影响搜索，但仍然建议做一些合理的分类）

## 其他类型的文档

考虑到实际使用时可能有不能录入成自维护内容的数据，本系统依然提供查看和全文检索功能。

目前支持全文检索的文档格式有：doc, docx, ppt, pptx, xls, xlsx, pdf, txt。特别说明：此类文档在查看时不保证原版格式；pdf会有换行导致段落文字不连续的问题，会影响全文检索结果。

除此等之外的任意文档格式只支持文件名级别的快捷检索，不支持内容级别的全文检索。

将任意文档存放在本系统的数据目录（在资源管理器右键显示本地文件位置）中，然后在资源处理器中刷新可同步本地更改，然后对新加入的文档或目录“重建索引”，待索引完成后即可进行检索，否则只支持查看。

## 四种检索功能

当收录了大量的数据后，要通过手工查找并浏览内容就不现实，此时最适当的方式是使用“搜索”，本程序提供四种搜索功能。

目前支持检索的语种：中文简体、中文繁体、日文、韩文、藏文（非桑布札）、英文系（10多种）。其他语种尚未测试。

### 1）快捷检索

     针对目录名、文件名、内容标题（主题）进行检索。

     通过快捷键“双击Shift”或“Ctrl/Command + G”开启。

### 2）全文检索

     针对内容文本进行检索。

     通过快捷键“Ctrl/Command + H”开启。

### 3）页内快捷检索

     仅在阅读视图（查看文档时）可用，针对所查看的文档内容标题进行查找并定位。

     在阅读视图中通过“Ctrl/Command + T”开启。

### 4）页内全文检索

     仅在阅读视图（查看文档）时可用，针对所查看的文档内容文本进行查找并定位。  

     在阅读视图中通过“Ctrl/Command + F”开启。
     
# 跨平台同功能的程序

提供三种平台的主程序，在[此处最新版](https://github.com/Yuanuo/smart-library/releases/latest)下载：

1. Windows版（仅64位）：smartLibrary-xx.xx.xx-Windows-x64.msi
2. MacOS版：smartLibrary-xx.xx.xx-MacOS.dmg
3. Linux版（Ubuntu、Debian等）：smart-library_xx.xx.xx_amd64-Linux.deb

暂时提供一个数据包：

1. 乾隆大藏经：[乾隆大藏经.smartlib](https://github.com/Yuanuo/smart-library/releases/download/22.02.07/LongZang-20220207_202505.smartlib)
  **特别说明，此数据来源于网络，未完全精确校对！**


# 详细功能说明

即将添加内容。。。
