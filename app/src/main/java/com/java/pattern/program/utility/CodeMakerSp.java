package com.java.pattern.program.utility;

import com.java.pattern.program.data.constant.AppConstant;

public class CodeMakerSp {

    public String CodeSyntaxMaker(String details){
        details = details.replace("&lt;", "<");
        details = details.replace("h&gt;", "h>");
        details = details.replace("<!-- wp:code -->\n<pre class=\"wp-block-code\"><code>", "");
        details = details.replace("</code></pre>\n<!-- /wp:code -->", "");
        return details;
    }

    public String Syntaxhighlighter(String details){
        return AppConstant.codemirrorCDN +
                AppConstant.textAreaStart +
                details +
                AppConstant.textAreaEnd +
                AppConstant.codeMirrorModify;
    }
}
