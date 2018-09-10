package io.github.abhishekwl.huntclient.Helpers;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

class NullOnEmptyConverterFactory extends Converter.Factory {
    public Converter responseBody(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
        return new Converter() {
            @Override
            public Object convert(@NonNull Object value) {
                return null;
            }

            public Object convert(ResponseBody body) {
                if (body.contentLength() == 0) return null;
                try {
                    return delegate.convert(body);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
}
