function upoload() {
    document.form1.uploadsub.disabled=false
    var result = false;
    var filepath = $("#filepath").val();
    var suffix = filepath.substring(filepath.lastIndexOf(".")+1,filepath.length);
    if (filepath != null && filepath != ""){

        alert(suffix.toLowerCase());
        if ("zip" == (suffix.toLowerCase())){
            result = true;
            document.form1.uploadsub.disabled=true;
        }else {
            alert("请上传zip文件")
        }
    }else {
        alert("上传文件不可以为空");
    }
    return result;
}
