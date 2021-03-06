package org.rundeck.client.util;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by greg on 5/20/16.
 */
public class QualifiedTypeConverterFactory extends Converter.Factory {
    private final Converter.Factory jsonFactory;
    private final Converter.Factory xmlFactory;
    private final Converter.Factory defaultFactory;

    public QualifiedTypeConverterFactory(
            Converter.Factory jsonFactory,
            Converter.Factory xmlFactory, final boolean defaultJson
    )
    {
        this.jsonFactory = jsonFactory;
        this.xmlFactory = xmlFactory;
        this.defaultFactory = defaultJson ? jsonFactory : xmlFactory;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations,
            Retrofit retrofit
    )
    {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Json) {
                return jsonFactory.responseBodyConverter(type, annotations, retrofit);
            }
            if (annotation instanceof Xml) {
                return xmlFactory.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return defaultFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit
    )
    {
        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof Json) {
                return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,
                                                        retrofit
                );
            }
            if (annotation instanceof Xml) {
                return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,
                                                       retrofit
                );
            }
        }
        return defaultFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations,
                                                   retrofit
        );
    }
}
