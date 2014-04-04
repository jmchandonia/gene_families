
package us.kbase.kbasegenefamilies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import us.kbase.common.service.Tuple4;
import us.kbase.common.service.Tuple5;


/**
 * <p>Original spec-file type: ProteinFamilyAnnotation</p>
 * <pre>
 * genome_ref genome - reference to genome
 * mapping<contig_id, list<tuple<string feature_id,int feature_start,int feature_stop,list<tuple<pfm_ref,int start_in_feature,int stop_in_feature,float evalue,float bitscore>>>>> data - 
 *         list of entrances of different protein families into proteins of this genome
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genome",
    "data"
})
public class ProteinFamilyAnnotation {

    @JsonProperty("genome")
    private java.lang.String genome;
    @JsonProperty("data")
    private Map<String, List<Tuple4 <String, Long, Long, List<Tuple5 <String, Long, Long, Double, Double>>>>> data;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("genome")
    public java.lang.String getGenome() {
        return genome;
    }

    @JsonProperty("genome")
    public void setGenome(java.lang.String genome) {
        this.genome = genome;
    }

    public ProteinFamilyAnnotation withGenome(java.lang.String genome) {
        this.genome = genome;
        return this;
    }

    @JsonProperty("data")
    public Map<String, List<Tuple4 <String, Long, Long, List<Tuple5 <String, Long, Long, Double, Double>>>>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Map<String, List<Tuple4 <String, Long, Long, List<Tuple5 <String, Long, Long, Double, Double>>>>> data) {
        this.data = data;
    }

    public ProteinFamilyAnnotation withData(Map<String, List<Tuple4 <String, Long, Long, List<Tuple5 <String, Long, Long, Double, Double>>>>> data) {
        this.data = data;
        return this;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return ((((((("ProteinFamilyAnnotation"+" [genome=")+ genome)+", data=")+ data)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
