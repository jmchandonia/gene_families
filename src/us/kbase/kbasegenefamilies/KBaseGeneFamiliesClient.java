package us.kbase.kbasegenefamilies;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonClientCaller;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.UnauthorizedException;

/**
 * <p>Original spec-file module name: KBaseGeneFamilies</p>
 * <pre>
 * </pre>
 */
public class KBaseGeneFamiliesClient {
    private JsonClientCaller caller;

    public KBaseGeneFamiliesClient(URL url) {
        caller = new JsonClientCaller(url);
    }

    public KBaseGeneFamiliesClient(URL url, AuthToken token) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(url, token);
    }

    public KBaseGeneFamiliesClient(URL url, String user, String password) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(url, user, password);
    }

    public URL getURL() {
        return caller.getURL();
    }

    public void setConnectionReadTimeOut(Integer milliseconds) {
        this.caller.setConnectionReadTimeOut(milliseconds);
    }

    public boolean isAuthAllowedForHttp() {
        return caller.isAuthAllowedForHttp();
    }

    public void setAuthAllowedForHttp(boolean isAuthAllowedForHttp) {
        caller.setAuthAllowedForHttp(isAuthAllowedForHttp);
    }

    public AuthToken getToken() {
        return caller.getToken();
    }

    public void _setFileForNextRpcResponse(File f) {
        caller.setFileForNextRpcResponse(f);
    }

    /**
     * <p>Original spec-file function name: search_domains</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link us.kbase.kbasegenefamilies.SearchDomainsParams SearchDomainsParams}
     * @return   parameter "job_id" of String
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public String searchDomains(SearchDomainsParams params) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<String>> retType = new TypeReference<List<String>>() {};
        List<String> res = caller.jsonrpcCall("KBaseGeneFamilies.search_domains", args, retType, true, true);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: construct_domain_clusters</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link us.kbase.kbasegenefamilies.ConstructDomainClustersParams ConstructDomainClustersParams}
     * @return   parameter "job_id" of String
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public String constructDomainClusters(ConstructDomainClustersParams params) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<String>> retType = new TypeReference<List<String>>() {};
        List<String> res = caller.jsonrpcCall("KBaseGeneFamilies.construct_domain_clusters", args, retType, true, true);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: search_domains_and_construct_clusters</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link us.kbase.kbasegenefamilies.SearchDomainsAndConstructClustersParams SearchDomainsAndConstructClustersParams}
     * @return   parameter "job_id" of String
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public String searchDomainsAndConstructClusters(SearchDomainsAndConstructClustersParams params) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<String>> retType = new TypeReference<List<String>>() {};
        List<String> res = caller.jsonrpcCall("KBaseGeneFamilies.search_domains_and_construct_clusters", args, retType, true, true);
        return res.get(0);
    }
}
