package us.kbase.kbasegenefamilies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.ServerException;
import us.kbase.common.service.Tuple11;
import us.kbase.common.service.Tuple2;
import us.kbase.common.service.Tuple3;
import us.kbase.common.service.Tuple4;
import us.kbase.common.service.Tuple5;
import us.kbase.common.service.UObject;
import us.kbase.kbasegenefamilies.util.Utils;
import us.kbase.kbasetrees.MSA;
import us.kbase.kbasetrees.MSASet;
import us.kbase.kbasetrees.MSASetElement;
import us.kbase.workspace.ListObjectsParams;
import us.kbase.workspace.ObjectIdentity;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.ProvenanceAction;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.SubObjectIdentity;
import us.kbase.workspace.WorkspaceClient;

public class ConstructDomainClustersBuilder extends DefaultTaskBuilder<ConstructDomainClustersParams> {
	private static final String domainModelWsType = "KBaseGeneFamilies.DomainModel";
	private static final String msaWsType = "KBaseTrees.MSA";
	private static final String domainClusterWsType = "KBaseGeneFamilies.DomainCluster";

	public ConstructDomainClustersBuilder() {
	}
	
	public ConstructDomainClustersBuilder(File tempDir, ObjectStorage objectStorage) {
		this.tempDir = tempDir;
		this.storage = objectStorage;
	}

	@Override
	public Class<ConstructDomainClustersParams> getInputDataType() {
		return ConstructDomainClustersParams.class;
	}
	
	@Override
	public String getOutRef(ConstructDomainClustersParams inputData) {
		return inputData.getOutWorkspace() + "/" + inputData.getOutResultId();
	}
	
	@Override
	public String getTaskDescription() {
		return "Search domains for one genome";
	}
	
	@Override
	public void run(String token, ConstructDomainClustersParams inputData, String jobId,
			String outRef) throws Exception {
		List<String> gaRefs = inputData.getGenomeAnnotations();
		
		//Tuple2<DomainAnnotation, DomainAlignments> res = dst.runDomainSearch(
		//		token, inputData.getDmsRef(), inputData.getGenome());
		//saveResult(inputData.getOutWorkspace(), inputData.getOutResultId(), token, res.getE1(), res.getE2(), inputData);
	}
	
	public static void constructClusters(String ws, String id, String token, Object inputData,
			GenomeAnnotationAlignmentProvider annAlnProv, String clustersForExtension, String dmsRef,
			boolean isGenomeAnnotationStoredOutside, String genomeAnnotationIdPrefix,
			String genomeAnnotationIdSuffix, boolean isDomainClusterDataStoredOutside,
			String domainClusterDataIdPrefix, String domainClusterDataIdSuffix,
			ObjectStorage storage) throws Exception {
		/*ProvenanceAction alnProv = new ProvenanceAction().withDescription(
				"Domain alignments for genome").withService("KBaseGeneFamilies")
				.withServiceVer(KBaseGeneFamiliesServer.SERVICE_VERSION)
				.withMethod("construct_multiple_alignment")
				.withMethodParams(Arrays.asList(new UObject(inputData)));
		String alignmentsRef = DomainSearchTask.getRefFromObjectInfo(storage.saveObjects(token,
				new SaveObjectsParams().withWorkspace(ws).withObjects(
				Arrays.asList(new ObjectSaveData().withType(DomainSearchTask.domainAlignmentsWsType)
						.withName(id + ".aln").withData(new UObject(alnRes))
						.withProvenance(Arrays.asList(alnProv))))).get(0));
		annRes.setAlignmentsRef(alignmentsRef);
		ObjectSaveData data = new ObjectSaveData().withData(new UObject(annRes))
				.withType(DomainSearchTask.domainAnnotationWsType)
				.withProvenance(Arrays.asList(new ProvenanceAction()
				.withDescription("Domain annotation was constructed using rps-blast program")
				.withService("KBaseGeneFamilies").withServiceVer(KBaseGeneFamiliesServer.SERVICE_VERSION)
				.withMethod("construct_multiple_alignment")
				.withMethodParams(Arrays.asList(new UObject(inputData)))));
		try {
			long objid = Long.parseLong(id);
			data.withObjid(objid);
		} catch (NumberFormatException ex) {
			data.withName(id);
		}
		storage.saveObjects(token, new SaveObjectsParams().withWorkspace(ws).withObjects(
				Arrays.asList(data)));*/
	}
	
	private static void addDomainsToClusters(ObjectStorage storage, String token, 
			GenomeAnnotationAlignmentProvider annAlnProv, String domainSetRef, 
			String parentDomainSetRef, Map<String, String> domainToParentCluster, 
			File tempDir, String jobId, Object inputParams,
			String outWsName, String outId, boolean isDomainClusterDataStoredOutside,
			String domainClusterDataIdPrefix, String domainClusterDataIdSuffix,
			String serviceMethod) throws Exception {
		if (domainSetRef == null)
			domainSetRef = parentDomainSetRef;
		File clusterDir = new File(new File(tempDir, "clusters"), "" + System.currentTimeMillis() + "_" + jobId);
		clusterDir.mkdirs();
		FileCache clusterCache = new FileCache(clusterDir);
		Map<String, GenomeStat> genomeRefToStat = new TreeMap<String, GenomeStat>();
		Set<String> domainRefs = new TreeSet<String>();
		Map<String, DomainAnnotation> genomeRefToAnnot = new TreeMap<String, DomainAnnotation>();
		Map<String, DomainAlignments> genomeRefToAlign = new TreeMap<String, DomainAlignments>();
		Map<String, String> genomeRefToAnnotRef = new TreeMap<String, String>();
		// Iterate over genomes (genome domain annotation objects)
		for (int pos = 0; pos < annAlnProv.size(); pos++) {
			Tuple3<String, DomainAnnotation, DomainAlignments> entry = annAlnProv.get(pos);
			String annRef = entry.getE1();
			DomainAnnotation annot = entry.getE2();
			String genomeRef = annot.getGenomeRef();
			String[] kBaseIdAndSciName = getGenomeIdAndScientificName(storage, token, genomeRef);
			DomainAlignments align = entry.getE3();
			if (annRef == null) {
				genomeRefToAnnot.put(genomeRef, annot);
				genomeRefToAlign.put(genomeRef, align);
			} else {
				genomeRefToAnnotRef.put(genomeRef, annRef);
			}
			int features = 0;
			int featuresWithDomains = 0;
			int domains = 0;
			// Iterate over contigs
			for (Map.Entry<String, List<Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>>>> contigElements : annot.getData().entrySet()) {
				String contigId = contigElements.getKey();
				// Iterate over features
				for (Tuple5<String, Long, Long, Long, Map<String, List<Tuple5<Long, Long, Double, Double, Double>>>> element : contigElements.getValue()) {
					String featureId = element.getE1();
					if (annot.getFeatureToContigAndIndex().get(featureId) == null) {
						if (element.getE5().isEmpty())
							continue;
						System.err.println("No feature index for [" + featureId + "] but domain map has size=" + element.getE5().size());
						continue;
					}
					long featureIndex = annot.getFeatureToContigAndIndex().get(featureId).getE2();
					features++;
					boolean featureWithDomains = false;
					// Iterate over domains
					for (Map.Entry<String, List<Tuple5<Long, Long, Double, Double, Double>>> domainPlaces : element.getE5().entrySet()) {
						String domainRef = domainPlaces.getKey();
						String clusterFile = "cluster_" + domainRef.replace('/', '_') + ".txt";
						for (Tuple5<Long, Long, Double, Double, Double> domainPlace : domainPlaces.getValue()) {
							String alignedSeq = align.getAlignments().get(domainRef).get(featureId).get("" + domainPlace.getE1());
							if (alignedSeq == null)
								throw new IllegalStateException("Can't find ailgnment for feature [" + featureId + "] for domain: " + domainRef);
							clusterCache.addLine(clusterFile, "[*]\t" + genomeRef + "\t" + contigId + "\t" + featureId + "\t" + 
									featureIndex + "\t" + domainPlace.getE1() + "\t" + domainPlace.getE2() + "\t" + 
									domainPlace.getE3() + "\t" + domainPlace.getE4() + "\t" + domainPlace.getE5() + "\t" + alignedSeq);
							domains++;
							featureWithDomains = true;
							domainRefs.add(domainRef);
						}
					}
					if (featureWithDomains)
						featuresWithDomains++;
				}
			}
			genomeRefToStat.put(genomeRef, new GenomeStat(genomeRef, kBaseIdAndSciName[0], kBaseIdAndSciName[1], 
					features, featuresWithDomains, domains));
			if (genomeRefToStat.size() % 30 == 0)
				clusterCache.flush();
		}
		clusterCache.flush();
		/////////////////////////////////////////////// Domains //////////////////////////////////////////////
		Map<String, DomainClusterStat> domainModelRefToStat = new TreeMap<String, DomainClusterStat>();
		Map<String, String> domainModelRefToNameMap = loadDomainRefToNameMap(tempDir, storage, token, domainSetRef);
		Map<String, String> domainModelRefToClusterRef = new TreeMap<String, String>();
		List<MSASetElement> innerMsas = new ArrayList<MSASetElement>();
		MSASet innerMsaSet = new MSASet().withDescription("MSA set containing data for domain sets construction")
				.withElements(innerMsas);
		for (String domainRef : domainRefs) {
			String clusterFile = "cluster_" + domainRef.replace('/', '_') + ".txt";
			String domainModelName = domainModelRefToNameMap.get(domainRef);
			if (domainModelName == null)
				throw new IllegalStateException("No domain model name for reference: " + domainRef);
			//String domainClusterName = domainModelName + ".cluster";
			System.out.println("Saving cluster and msa for domain model: " + domainModelName + "(" + domainRef + ")");
			Set<String> genomeRefAndFeatureIdAndStart = new HashSet<String>();
			Map<String, Tuple4<String, String, Long, List<Tuple5<Long, Long, Double, Double, Double>>>> genomeRefAndFeatureIdToElement = 
					new HashMap<String, Tuple4<String, String, Long, List<Tuple5<Long, Long, Double, Double, Double>>>>();
			DomainCluster cluster = new DomainCluster().withModel(domainRef).withData(new TreeMap<String, 
					List<Tuple4<String, String, Long, List<Tuple5<Long, Long, Double, Double, Double>>>>>());
			MSA msa = new MSA().withAlignment(new TreeMap<String, String>());
			int alnLen = 0;
			BufferedReader br = new BufferedReader(new FileReader(clusterFile));
			try {
				while (true) {
					String l = br.readLine();
					if (l == null)
						break;
					String[] parts = l.split("\t");
					if (parts.length != 11) {
						if (l.lastIndexOf("[*]\t") <= 0)
							throw new IllegalStateException("Wrong line format: \"" + l + "\"");
						l = l.substring(l.lastIndexOf("[*]\t"));
						parts = l.split("\t");
					}
					String genomeRef = parts[1];
					String contigId = parts[2];
					String featureId = parts[3]; 
					long featureIndex = Long.parseLong(parts[4]);
					Tuple5<Long, Long, Double, Double, Double> domainPlace = new Tuple5<Long, Long, Double, Double, Double>()
							.withE1(Long.parseLong(parts[5])).withE2(Long.parseLong(parts[6])).withE3(Double.parseDouble(parts[7]))
							.withE4(Double.parseDouble(parts[8])).withE5(Double.parseDouble(parts[9]));
					String alignedSeq = parts[10];
					String key = genomeRef + "_" + featureId + "_" + domainPlace.getE1();
					if (genomeRefAndFeatureIdAndStart.contains(key))
						continue;
					msa.getAlignment().put(key, alignedSeq);
					alnLen = alignedSeq.length();
					List<Tuple4<String, String, Long, List<Tuple5<Long, Long, Double, Double, Double>>>> elements = 
							cluster.getData().get(genomeRef);
					if (elements == null) {
						elements = new ArrayList<Tuple4<String, String, Long, List<Tuple5<Long, Long, Double, Double, Double>>>>();
						cluster.getData().put(genomeRef, elements);
					}
					String key2 = genomeRef + "_" + featureId;
					Tuple4<String, String, Long, List<Tuple5<Long, Long, Double, Double, Double>>> element = 
							genomeRefAndFeatureIdToElement.get(key2);
					if (element == null) {
						element = new Tuple4<String, String, Long, List<Tuple5<Long, Long, Double, Double, Double>>>()
								.withE1(contigId).withE2(featureId).withE3(featureIndex)
								.withE4(new ArrayList<Tuple5<Long, Long, Double, Double, Double>>());
						elements.add(element);
						genomeRefAndFeatureIdToElement.put(key2, element);
					}
					element.getE4().add(domainPlace);
				}
			} finally {
				br.close();
			}
			msa.setAlignmentLength((long)alnLen);
			if (isDomainClusterDataStoredOutside) {
				String domainClusterName = domainModelName;
				if (!domainClusterDataIdPrefix.isEmpty())
					domainClusterName = domainClusterDataIdPrefix + "." + domainClusterName;
				if (!domainClusterDataIdSuffix.isEmpty())
					domainClusterName = domainClusterName + "." + domainClusterDataIdSuffix;
				///// MSA
				String msaRef = saveObject(storage, token, outWsName, msaWsType, domainClusterName + ".msa", 
						msa, "Object was constructed as part of domain clusters construction procedure",
						serviceMethod, inputParams);
				///// DomainCluster
				cluster.setMsaRef(msaRef);
				String clusterRef = saveObject(storage, token, outWsName, msaWsType, domainClusterName + ".cluster", 
						msa, "Object was constructed as part of domain clusters construction procedure",
						serviceMethod, inputParams);
				domainModelRefToClusterRef.put(domainRef, clusterRef);
			}
		}
		DomainClusterSearchResult res = new DomainClusterSearchResult();
		res.setUsedDmsRef(domainSetRef);
		res.setAnnotations(genomeRefToAnnot);
		res.setAlignments(genomeRefToAlign);
		res.setAnnotationRefs(genomeRefToAnnotRef);
		//res.setDomainClusters(domainClusters)
		res.setDomainClusterRefs(domainModelRefToClusterRef);
		//saveObject(client, domainWsName, domainClusterSearchResultType, defaultDCSRObjectName, res);
	}
		
	private static String saveObject(ObjectStorage storage, String token, String ws, String type, String id, 
			Object obj, String provDescr, String serviceMethod, Object inputParams) throws Exception {
		ProvenanceAction prov = new ProvenanceAction().withDescription(provDescr)
				.withService("KBaseGeneFamilies").withServiceVer(KBaseGeneFamiliesServer.SERVICE_VERSION)
				.withMethod(serviceMethod).withMethodParams(Arrays.asList(new UObject(inputParams)));
		ObjectSaveData data = new ObjectSaveData().withData(new UObject(obj))
				.withType(type).withProvenance(Arrays.asList(prov));
		try {
			long objid = Long.parseLong(id);
			data.withObjid(objid);
		} catch (NumberFormatException ex) {
			data.withName(id);
		}
		return DomainSearchTask.getRefFromObjectInfo(storage.saveObjects(token, 
				new SaveObjectsParams().withWorkspace(ws).withObjects(Arrays.asList(data))).get(0));
	}

	private static String[] getGenomeIdAndScientificName(ObjectStorage storage, String token, 
			String genomeRef) throws Exception {
		Map<String, String> genome = storage.getObjectSubset(token, Arrays.asList(
				new SubObjectIdentity().withRef(genomeRef).withIncluded(Arrays.asList(
						"id", "scientific_name")))).get(0).getData().asInstance();
		String id = genome.get("id");
		String name = genome.get("scientific_name");
		return new String[] {id, name};
	}
	
	private static Map<String, String> loadDomainRefToNameMap(File tempDir, ObjectStorage storage, 
			String token, String domainModelSetRef) throws Exception {
		return new DomainSearchTask(tempDir, storage).loadDomainModelRefToNameMap(token, domainModelSetRef);
	}
	
	public static List<Tuple2<String, String>> listAllObjectsRefAndName(ObjectStorage client, 
			String token, String wsName, String wsType) throws Exception {
		List<Tuple2<String, String>> ret = new ArrayList<Tuple2<String, String>>();
		for (int partNum = 0; ; partNum++) {
			int sizeOfPart = 0;
			for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : 
				client.listObjects(token, new ListObjectsParams().withWorkspaces(Arrays.asList(wsName))
						.withType(wsType).withLimit(10000L).withSkip(partNum * 10000L))) {
				String ref = Utils.getRefFromObjectInfo(info);
				String objectName = info.getE2();
				ret.add(new Tuple2<String, String>().withE1(ref).withE2(objectName));
				sizeOfPart++;
			}
			if (sizeOfPart == 0)
				break;
		}
		return ret;
	}

	public static class GenomeAnnotationAlignmentProvider {
		private List<String> annotRefs;
		private List<Tuple2<DomainAnnotation, DomainAlignments>> annotAndAln;
		private ObjectStorage storage;
		private String token;
		
		public GenomeAnnotationAlignmentProvider(List<String> annotRefs,
				List<Tuple2<DomainAnnotation, DomainAlignments>> annotAndAln,
				ObjectStorage storage, String token) {
			this.annotRefs = annotRefs;
			if (this.annotRefs == null)
				this.annotRefs = Collections.<String>emptyList();
			this.annotAndAln = annotAndAln;
			if (this.annotAndAln == null)
				this.annotAndAln = Collections.<Tuple2<DomainAnnotation, DomainAlignments>>emptyList();
			this.storage = storage;
		}
		
		public int size() {
			return annotRefs.size() + annotAndAln.size();
		}
		
		public Tuple3<String, DomainAnnotation, DomainAlignments> get(int index) throws Exception {
			if (index < annotRefs.size()) {
				String annotRef = annotRefs.get(index);
				DomainAnnotation ann = storage.getObjects(token, Arrays.asList(
						new ObjectIdentity().withRef(annotRef))).get(0).getData().asClassInstance(
								DomainAnnotation.class);
				DomainAlignments aln = storage.getObjects(token, Arrays.asList(
						new ObjectIdentity().withRef(ann.getAlignmentsRef()))).get(0).getData().asClassInstance(
								DomainAlignments.class);
				return new Tuple3<String, DomainAnnotation, DomainAlignments>().withE1(annotRef).withE2(ann).withE3(aln);
			} else {
				Tuple2<DomainAnnotation, DomainAlignments> ret = annotAndAln.get(index - annotRefs.size());
				return new Tuple3<String, DomainAnnotation, DomainAlignments>().withE2(ret.getE1()).withE3(ret.getE2());
			}
		}
	}
	
	public static class FileCache {
		private File workDir;
		private Map<String, List<String>> file2lines = new LinkedHashMap<String, List<String>>();
		
		public FileCache(File dir) {
			workDir = dir;
		}
		
		public void addLine(String file, String l) {
			List<String> lines = file2lines.get(file);
			if (lines == null) {
				lines = new ArrayList<String>();
				file2lines.put(file, lines);
			}
			lines.add(l);
		}
		
		public void flush() throws IOException {
			if (workDir == null)
				return;
			for (String fileName : file2lines.keySet()) {
				File f = new File(workDir, fileName);
				PrintWriter clusterPw = new PrintWriter(new FileWriter(f, true));
				try {
					for (String l : file2lines.get(f))
						clusterPw.println(l);
					file2lines.get(f).clear();
				} finally {
					clusterPw.close();
				}
			}
		}
		
		public List<String> getLines(String file) throws IOException {
			if(workDir == null) {
				return file2lines.get(file);
			} else {
				List<String> ret = new ArrayList<String>();
				BufferedReader br = new BufferedReader(new FileReader(new File(workDir, file)));
				while (true) {
					String l = br.readLine();
					if (l == null)
						break;
					ret.add(l);
				}
				br.close();
				return ret;
			}
		}
	}
	
	public static interface DomainClusterConsumer {
		public String storeDomainClusterGetRef(DomainCluster cluster) throws Exception;
	}
	
	private static class GenomeStat {
		String genomeRef;
		String kBaseId;
		String scientificName;
		int features;
		int featuresWithDomains;
		int domains;
		
		public GenomeStat(String genomeRef, String kBaseId, String scientificName, 
				int features, int featuresWithDomains, int domains) {
			this.genomeRef = genomeRef;
			this.kBaseId = kBaseId;
			this.scientificName = scientificName;
			this.features = features;
			this.featuresWithDomains = featuresWithDomains;
			this.domains = domains;
		}
	}

	private static class DomainClusterStat {
		String domainModelRef;
		String name;
		int genomes;
		int domains;
	}
}
