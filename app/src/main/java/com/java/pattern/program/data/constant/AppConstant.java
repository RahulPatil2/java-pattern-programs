package com.java.pattern.program.data.constant;

public class AppConstant {

    public static final String EMPTY_STRING = "";
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_MESSAGE = "message";
    public static final String BUNDLE_KEY_URL = "url";
    public static final String BUNDLE_FROM_PUSH = "from_push";
    public static final long SITE_CACHE_SIZE = 10 * 1024 * 1024;

    public static final String NEW_NOTI = "new_notification";
    public static final String BUNDLE_KEY_ITEM = "item";
    public static final String BUNDLE_KEY_INDEX = "position";
    public static final String BUNDLE_KEY_ID = "id";
    public static final String BUNDLE_KEY_DELETE_ALL_NOT = "delete_all_not";

    public static final String BUNDLE_KEY_YES = "yes";
    public static final String BUNDLE_KEY_NO = "no";
    public static final String BUNDLE_KEY_VIEW_ID = "view_id_tex";
    public static final String BUNDLE_KEY_DIALOG_FRAGMENT = "dialog_fragment";
    public static final String BUNDLE_KEY_EXIT_OPTION = "exit";
    public static final String BUNDLE_KEY_DELETE_EACH_FAV = "delete_each_fav";
    public static final String BUNDLE_KEY_DELETE_ALL_FAV = "delete_all_fav";
    public static final String BUNDLE_KEY_CATEGORY_ID = "category_id";

    public static final int BUNDLE_KEY_ZERO_INDEX = 0;
    public static final int BUNDLE_KEY_FIRST_INDEX = 1;
    public static final int ADS_INTERVAL = 2;
    public static final int CLICK_COUNT = 3;


    public static final String TTS_LOCALE = "en_US";


    // category file
    public static final String CATEGORY_FILE = "json/content/categories.json";
    public static final String JSON_KEY_CATEGORY_ID = "category_id";
    public static final String JSON_KEY_CATEGORY_TITLE = "category_name";


    // content file
    public static final String CONTENT_FILE = "json/content/contents.json";
    public static final String JSON_KEY_ITEMS = "items";
    public static final String JSON_KEY_TITLE = "title";
    public static final String JSON_KEY_CATEGORY = "category";
    public static final String JSON_KEY_DETAILS = "details";


    // notification constants
    public static final String PREF_NOTIFICATION = "perf_notification";
    public static final String PREF_FONT_SIZE = "pref_font_size";

    //styles
    public static final String CSS = "<style>\n" +
            ".wp-block-code {\n" +
            "    box-sizing: border-box;\n" +
            "}\n" +
            "\n" +
            "pre {\n" +
            "    font-family: \"Courier 10 Pitch\", Courier, monospace;\n" +
            "    font-size: 0.9375rem;\n" +
            "    line-height: 1.6;\n" +
            "    max-width: 100%;\n" +
            "    overflow: auto;\n" +
            "    padding: 1.5em;\n" +
            "    white-space: pre-wrap;\n" +
            "}\n" +
            "</style>";
    public static final String codemirrorCDN ="\n" +
            "<link rel=\"stylesheet\" href=\"file:///android_asset/CSS/codemirror.min.css\">\n" +
            "  <script src=\"file:///android_asset/JS/codemirror.min.js\"></script>\n" +
            "  <script src=\"file:///android_asset/JS/clike.min.js\"></script>\n";
    public static final String textAreaStart = "<textarea id=\"codeEditor\">";
    public static final String textAreaEnd = "</textarea>";
    public static final String codeMirrorModify = "  <script>\n" +
            "    // Get the textarea element\n" +
            "    var codeEditor = document.getElementById(\"codeEditor\");\n" +
            "\n" +
            "    // Initialize CodeMirror with C mode\n" +
            "    var editor = CodeMirror.fromTextArea(codeEditor, {\n" +
            "      mode: \"text/x-csrc\",\n" +
            "      theme: \"default\", // You can choose a different theme if desired\n" +
            "      lineNumbers: true // To show line numbers in the editor\n" +
            "    });\n" +
            "  </script>";

}
