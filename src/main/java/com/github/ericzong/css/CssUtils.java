package com.github.ericzong.css;

import com.steadystate.css.dom.CSSRuleListImpl;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSStyleSheetImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CssUtils {

    public static List<CSSStyleSheet> getSheets(String... texts) {
        if (texts == null || texts.length == 0) {
            return Collections.EMPTY_LIST;
        }

        List<CSSStyleSheet> sheets = Arrays.stream(texts).map(t -> {
            try {
                return getSheet(t);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        return sheets;
    }

    public static CSSStyleSheet getSheet(String text) throws IOException {
        InputSource source = new InputSource(new StringReader(text));
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        return sheet;
    }

    public static List<CSSRule> getRules(CSSStyleSheet sheet) {
        CSSRuleList rules = sheet.getCssRules();

        // Default implementation
        if (rules instanceof CSSRuleListImpl) {
            return ((CSSRuleListImpl) rules).getRules();
        }

        // Maybe never execute
        List<CSSRule> cssRules = new ArrayList<>(rules.getLength());
        for (int i = 0; i < rules.getLength(); i++) {
            cssRules.add(rules.item(i));
        }

        return cssRules;
    }

    public static Map<String, String> getStyleMap(CSSRule cssRule) {
        Map<String, String> styleMap;

        if (cssRule instanceof CSSStyleRule) {
            CSSStyleRule styleRule = ((CSSStyleRule) cssRule);
            CSSStyleDeclaration styles = styleRule.getStyle();

            styleMap = new LinkedHashMap<>();
            for (int i = 0; i < styles.getLength(); i++) {
                String prop = styles.item(i);
                String value = styles.getPropertyValue(prop);
                styleMap.put(prop, value);
            }
        } else {
            styleMap = Collections.emptyMap();
        }

        return styleMap;
    }

    public static String getSelector(CSSRule rule) {
        String selector = "";
        if (rule instanceof CSSStyleRule) {
            selector = ((CSSStyleRule) rule).getSelectorText();
        }

        return selector;
    }

    public static CSSStyleSheet mergeSheets(List<CSSStyleSheet> styleSheets) {
        CSSStyleSheet mergeSheet = new CSSStyleSheetImpl();
        int index = 0;
        for (CSSStyleSheet styleSheet : styleSheets) {
            CSSRuleList rules = styleSheet.getCssRules();
            for (int i = 0; i < styleSheet.getCssRules().getLength(); i++) {
                CSSRule rule = rules.item(i);
                mergeSheet.insertRule(rule.getCssText(), index++);
            }
        }

        return mergeSheet;
    }

    public static CSSStyleSheet mergeSheet(TagNode page) throws XPatherException {
        Object[] styleObjs = page.evaluateXPath("//style");
        List<CSSStyleSheet> sheets = Arrays.stream(styleObjs).map(o -> {
            try {
                return getSheet(((TagNode) o).getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());

        return mergeSheets(sheets);
    }

    public static CSSStyleSheet cleanSheet(TagNode page, CSSStyleSheet mergeSheet) throws XPatherException {
        CSSStyleSheet cleanSheet = new CSSStyleSheetImpl();

        List<CSSRule> rules = CssUtils.getRules(mergeSheet);
        int i = 0;
        for (CSSRule rule : rules) {
            CSSRule cleanRule = cleanRule(page, rule);
            if(cleanRule != null) {
                cleanSheet.insertRule(cleanRule.getCssText(), i++);
            }
        }

        return cleanSheet;
    }

    public static CSSRule cleanRule(TagNode page, CSSRule rule) throws XPatherException {
        String selectorName = getSelector(rule);
        String[] selectors = selectorName.split(" *, *");

        List<String> usedSelectors = new ArrayList<>();
        for (String s : selectors) {
            if(isUsed(page, s)) {
                usedSelectors.add(s);
            }
        }

        if(usedSelectors.size() > 0) {
            String usedSelector = usedSelectors.stream().collect(Collectors.joining(", "));
            ((CSSStyleRuleImpl) rule).setSelectorText(usedSelector);
            return rule;
        }

        return null;
    }

    private static boolean isUsed(TagNode page, String s) throws XPatherException {
        String[] levels = s.split(" +");
        for (String level : levels) {
            if(level.startsWith("#")) {
                String id = level.substring(1);
                Object[] objs = page.evaluateXPath(String.format("//*[@id='%s']", id));
                if(objs.length == 0) {
                    System.out.println(String.format("selector: %s, id: %s", s, id));
                    return false;
                }
            } else if(level.indexOf('.') != -1) {
                String className = level.substring(level.indexOf('.') + 1);
                Object[] objs = page.evaluateXPath(String.format("//*[@class='%s']", className));
                if(objs.length == 0) {
                    System.out.println(String.format("selector: %s, class: %s", s, className));
                    return false;
                }
            }
        }

        return true;
    }


}
