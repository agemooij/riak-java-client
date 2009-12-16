/*
This file is provided to you under the Apache License,
Version 2.0 (the "License"); you may not use this file
except in compliance with the License.  You may obtain
a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.  
*/
package com.basho.riak.client.jiak;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.basho.riak.client.RiakClient;
import com.basho.riak.client.RiakConfig;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakObject;
import com.basho.riak.client.request.RequestMeta;
import com.basho.riak.client.request.RiakWalkSpec;
import com.basho.riak.client.response.BucketResponse;
import com.basho.riak.client.response.HttpResponse;
import com.basho.riak.client.response.StreamHandler;
import com.basho.riak.client.util.ClientHelper;
import com.basho.riak.client.util.Constants;

public class JiakClient implements RiakClient {
    
    private ClientHelper helper;
    
    public JiakClient(RiakConfig config) {
        this.helper = new ClientHelper(config);
    }

    public JiakClient(String url) { 
        this.helper = new ClientHelper(new RiakConfig(url));
    }
    
    public HttpResponse setBucketSchema(String bucket,
            List<String> allowedFields, List<String> writeMask,
            List<String> readMask, List<String> requiredFields, RequestMeta meta) {
        return helper.setBucketSchema(bucket, allowedFields, writeMask, readMask, requiredFields, meta);
    }
    public HttpResponse setBucketSchema(String bucket,
            List<String> allowedFields, List<String> writeMask,
            List<String> readMask, List<String> requiredFields) {
        return setBucketSchema(bucket, allowedFields, writeMask, readMask, requiredFields, null);
    }

    public BucketResponse listBucket(String bucket, RequestMeta meta) {
        return helper.listBucket(bucket, meta);
    }
    public BucketResponse listBucket(String bucket) {
        return listBucket(bucket, null);
    }

    public JiakStoreResponse store(RiakObject object, RequestMeta meta) {
        if (meta == null) meta = new RequestMeta();
        meta.put(Constants.HDR_CONTENT_TYPE, Constants.CTYPE_JSON);
        meta.put(Constants.HDR_ACCEPT, Constants.CTYPE_JSON);
        meta.addQueryParam(Constants.QP_RETURN_BODY, "true");
        try {
            return new JiakStoreResponse(helper.store(object, meta));
        } catch (JSONException e) {
            throw new RiakException(e);
        }
    }
    public JiakStoreResponse store(RiakObject object) {
        return store(object, null);
    }

    public JiakFetchResponse fetchMeta(String bucket, String key, RequestMeta meta) {
        // Jiak doesn't support HEAD, so just fetch()
        return fetch(bucket, key, meta); 
    }
    public JiakFetchResponse fetchMeta(String bucket, String key) {
        return fetchMeta(bucket, key, null);
    }

    public JiakFetchResponse fetch(String bucket, String key, RequestMeta meta) {
        if (meta == null) meta = new RequestMeta();
        meta.put(Constants.HDR_ACCEPT, Constants.CTYPE_JSON);
        try {
            return new JiakFetchResponse(helper.fetch(bucket, key, meta));
        } catch (JSONException e) {
            throw new RiakException(e);
        }
    }
    public JiakFetchResponse fetch(String bucket, String key) {
        return fetch(bucket, key, null);
    }

    public boolean stream(String bucket, String key, StreamHandler handler,
            RequestMeta meta) throws IOException {
        return helper.stream(bucket, key, handler, meta);
    }

    public HttpResponse delete(String bucket, String key, RequestMeta meta) {
        return helper.delete(bucket, key, meta);
    }
    public HttpResponse delete(String bucket, String key) {
        return delete(bucket, key, null);
    }

    public JiakWalkResponse walk(String bucket, String key, String walkSpec, RequestMeta meta) {
        if (meta == null) meta = new RequestMeta();
        meta.put(Constants.HDR_ACCEPT, Constants.CTYPE_JSON);
        try {
            return new JiakWalkResponse(helper.walk(bucket, key, walkSpec, meta));
        } catch (JSONException e) {
            throw new RiakException(e);
        }
    }
    public JiakWalkResponse walk(String bucket, String key, String walkSpec) {
        return walk(bucket, key, walkSpec, null);
    }
    public JiakWalkResponse walk(String bucket, String key, RiakWalkSpec walkSpec) {
        return walk(bucket, key, walkSpec.toString(), null);
    }
}