package org.jent.when

import org.jent.core.when.WhenPolicy

class BranchPatternPolicy implements WhenPolicy {

    List<String> patterns = ["main"]

    @Override
    boolean allows(def script) {
        String branch = script?.env?.BRANCH_NAME?.toString() ?: ""
        if (!branch) {
            return false
        }

        for (String pattern : normalizePatterns(patterns)) {
            if (matchesGlob(branch, pattern)) {
                return true
            }
        }
        return false
    }

    private static List<String> normalizePatterns(def raw) {
        if (raw == null) {
            return []
        }
        if (raw instanceof String) {
            return raw.split(/[\s,]+/).collect { it?.trim() }.findAll { it }
        }
        return raw.collect { it?.toString()?.trim() }.findAll { it }
    }

    private static boolean matchesGlob(String value, String pattern) {
        String regex = globToRegex(pattern)
        return value ==~ regex
    }

    private static String globToRegex(String pattern) {
        StringBuilder out = new StringBuilder("^")
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i)
            if (c == '*') {
                out.append(".*")
            } else if (c == '?') {
                out.append('.')
            } else if ('\\.^$|()[]{}+'.indexOf(String.valueOf(c)) >= 0) {
                out.append('\\').append(c)
            } else {
                out.append(c)
            }
        }
        out.append('$')
        return out.toString()
    }
}
