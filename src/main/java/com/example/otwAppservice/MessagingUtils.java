package com.example.otwAppservice;

public class MessagingUtils {


    public static String transformSms(String phone, String from, String token, String messageText) {

//        return "{\"messages\":{\"authentication\":{\"productToken\":\"" + token + "\"},\"msg\":[{\"body\":{\"content\":\"" + messageText + "\"},\"from\":\"" + from + "\",\"to\":[{\"number\":\"" + phone + "\"}]}]}}";
        return "{\n" +
                "    \"messages\": {\n" +
                "        \"authentication\": {\n" +
                "            \"productToken\": \"" + token + "\"\n" +
                "        },\n" +
                "        \"msg\": [\n" +
                "            {\n" +
                "                \"from\": \"" + from + "\",\n" +
                "                \"body\": {\n" +
                "                    \"content\": \"" + messageText + "\",\n" +
                "                    \"type\": \"auto\"\n" +
                "                },\n" +
                "                \"minimumNumberOfMessageParts\": 1,\n" +
                "                \"maximumNumberOfMessageParts\": 8,\n" +
                "                \"to\": [\n" +
                "                    {\n" +
                "                        \"number\": \"" + phone + "\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"alowedChannels\": [\n" +
                "                    \"SMS\"\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";


    }

}
