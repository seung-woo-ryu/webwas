package org.example.util;

import org.example.servlet.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpParser {
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_POST = "POST";
    private static final int START_LINE = 0;
    private static final Long EMPTY_LINE_NOT_EXIST = -1L;

    private static final String START_LINE_DELIMITER = " ";
    private static final String HEADER_LINE_DELIMITER = ": ";
    private static final String URL_PARAMS_DELIMITER = "\\?";
    private static final String BETWEEN_PARAMS_DELIMITER = "&";
    private static final String PARAM_KEY_VALUE_DELIMITER = "=";

    private static final int METHOD_IDX = 0;
    private static final int REQUEST_TARGET_IDX = 1;
    private static final int HTTP_VERSION_IDX = 2;
    /*
     * Util class should not create instance
     */
    private HttpParser() {

    }

    public static void parse(String req, HttpServletRequest httpServletRequest) {
        List<String> reqLines = req.lines().collect(Collectors.toList());

        Long emptyLine = findEmptyLine(reqLines);

        parseStartLine(reqLines,httpServletRequest);
        parseHeaders(reqLines,httpServletRequest,emptyLine);

        String method = httpServletRequest.getMethod();

        if ((METHOD_POST.equals(method) || METHOD_PUT.equals(method)) && existBody(emptyLine)) {
            parseBody(reqLines,httpServletRequest,emptyLine);
        }
    }
    private static void parseBody(List<String> req, HttpServletRequest httpServletRequest,Long emptyLine) {
        int bodyStartLineNum = emptyLine.intValue() + 1;
        int bodyLastLineNum = req.size();

        List<String> bodyLines = req.subList(bodyStartLineNum, bodyLastLineNum);

        String concatBody = String.join("\n", bodyLines);

        httpServletRequest.setBody(concatBody);
    }

    private static void parseHeaders(List<String> req, HttpServletRequest httpServletRequest,Long emptyLine) {
        Map<String, Object> headers = new HashMap<>();

        int lastIdx = req.size();

        if(existBody(emptyLine)){
            lastIdx = emptyLine.intValue();
        }

        for (int i = 1; i < lastIdx; i++) {
            getHeader(req.get(i),headers);
        }

        httpServletRequest.setHeaders(headers);
    }
    private static void parseStartLine(List<String> reqLines, HttpServletRequest httpServletRequest) {
        String[] splitStartLine = reqLines.get(START_LINE).split(START_LINE_DELIMITER);

        httpServletRequest.setMethod(splitStartLine[METHOD_IDX]);
        httpServletRequest.setHttpVersion(splitStartLine[HTTP_VERSION_IDX]);
        setUrlAndQueryParams(splitStartLine[REQUEST_TARGET_IDX], httpServletRequest);
    }

    private static void setUrlAndQueryParams(String target, HttpServletRequest httpServletRequest) {
        String url = target;
        Map<String,List<String>> multiValueParams = new HashMap<>();

        if (target.contains("?")) {
            String[] split = target.split(URL_PARAMS_DELIMITER);

            url = split[0];
            parseParams(split[1],multiValueParams);
        }

        httpServletRequest.setRequestTarget(url);
        httpServletRequest.setParams(multiValueParams);
    }

    private static void parseParams(String params, Map<String,List<String>> multiValueParams) {
        for (String param : params.split(BETWEEN_PARAMS_DELIMITER)) {
            String[] keyValue = param.split(PARAM_KEY_VALUE_DELIMITER);

            List<String> values = multiValueParams.getOrDefault(keyValue[0], new ArrayList<String>());

            values.add(keyValue[1]);
            multiValueParams.put(keyValue[0], values);
        }
    }


    private static Long findEmptyLine(List<String> reqLines) {
        for (int i = 0; i < reqLines.size(); i++) {
            if (reqLines.get(i).isBlank()) {
                return Long.valueOf(i);
            }
        }
        return EMPTY_LINE_NOT_EXIST;
    }
    private static void getHeader(String headerLine, Map<String, Object> headers) {
        String[] splitHeader = headerLine.split(HEADER_LINE_DELIMITER);

        headers.put(splitHeader[0], splitHeader[1]);
    }
    private static boolean existBody(Long emptyLine) {
        return !emptyLine.equals(EMPTY_LINE_NOT_EXIST);
    }
}
