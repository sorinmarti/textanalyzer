package com.sm.textanalyzer.app;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("HardCodedStringLiteral")
public class ProjectFileManager {
	
	/**
	 * Writes a XML file containing project data.
	 * @param projectFile The file containing the save path in the file system.
	 * @param project The project file to serialize
	 * @throws Exception Thrown if XML file can't be created or saved.
	 */
	public static void writeProjectFile(File projectFile, Project project) throws Exception {
		// TODO Only paths get saved to xml. Add lemmaLibrary
		
		try {
			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
	        DocumentBuilder build = dFact.newDocumentBuilder();
	        Document doc = build.newDocument();

	        Element root = doc.createElement("project");
	        doc.appendChild(root);

	        Element corpusNode = doc.createElement("corpus");
	        corpusNode.setAttribute("name", project.getCorpus().getName());
	        root.appendChild(corpusNode);

	        for(CorpusCollection collection : project.getCorpus().getCollections()) {
                Element collectionNode = doc.createElement("collection");
                collectionNode.setAttribute("name", collection.getName());
                collectionNode.setAttribute("accepts", collection.getAcceptedType().getXmlTypeName());
                for(CorpusFile file : collection.getFiles()) {
                    Element fileNode = doc.createElement("file");
                    fileNode.setAttribute("name", file.getName());
                    fileNode.setAttribute("type", file.getFileType().getXmlTypeName());
                    fileNode.setAttribute("date", file.getFileDateString());
                    if(file.getAuthor()!=null) {
                        fileNode.setAttribute("author", file.getAuthor().getId());
                    }
                    fileNode.appendChild(doc.createTextNode( file.getPath().toString() ));
                    collectionNode.appendChild( fileNode );
                }
                corpusNode.appendChild( collectionNode );
            }

            Element authorsNode = doc.createElement("authors");
	        for(Author author : project.getAuthors()) {
                Element authorNode = doc.createElement("author");
                authorNode.setAttribute("name", author.getName());
                authorNode.setAttribute("ageMin", String.valueOf(author.getAgeMin()));
                authorNode.setAttribute("ageMax", String.valueOf(author.getAgeMax()));
                authorNode.setAttribute("gender", String.valueOf(author.getGender()));
                authorsNode.appendChild(authorNode);
	        }
            root.appendChild(authorsNode);

            Element languagesNode = doc.createElement("languages");
            for(Language language : project.getLanguages()) {
                Element languageNode = doc.createElement("language");
                languageNode.setAttribute("name", language.getName());
                languagesNode.appendChild(languageNode);
            }
            root.appendChild(languagesNode);

	        // Save the document to the disk file
	        TransformerFactory tranFactory = TransformerFactory.newInstance();
	        Transformer aTransformer = tranFactory.newTransformer();

	        // format the XML nicely
	        aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

	        aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        DOMSource source = new DOMSource(doc);
	        try {
	            // location and name of XML file you can change as per need
	            FileWriter fos = new FileWriter( projectFile );
	            StreamResult result = new StreamResult(fos);
	            aTransformer.transform(source, result);

	        } catch (IOException e) {

	            e.printStackTrace();
	        }

	    }  catch (TransformerException e) {
	    	throw new Exception("Error outputting project file:" + e.getMessage());

	    } catch (ParserConfigurationException e) {
	    	throw new Exception("Error building project file:" + e.getMessage());
	    }
	}
	
	/**
	 * Reads a project object from an XML file
	 * @param projectFile The file in the file system to read into the XML
	 * @return The parsed project file
	 * @throws Exception Thrown if file could not be opened or parsed.
	 */
	public static Project readProjectFile(File projectFile) throws Exception {
		Project project = new Project();
		project.setProjectFile( projectFile );
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(projectFile);

			//optional, but recommended
			doc.getDocumentElement().normalize();

			Node corpusNode = doc.getElementsByTagName("corpus").item(0);
			project.getCorpus().setName( corpusNode.getAttributes().getNamedItem("name").getTextContent() );

			NodeList corpusNodeChildren = corpusNode.getChildNodes();
			for (int temp = 0; temp < corpusNodeChildren.getLength(); temp++) {
				Node nodeCorpusChild = corpusNodeChildren.item(temp);
				if(nodeCorpusChild.getNodeName().equals("collection")) {
                    CorpusCollection collection = new CorpusCollection( nodeCorpusChild.getAttributes().getNamedItem("name").getTextContent() );
                    collection.setAcceptedType( CorpusFileType.fromXML( nodeCorpusChild.getAttributes().getNamedItem("accepts").getTextContent() ) );

                    NodeList fileList = nodeCorpusChild.getChildNodes();
                    for(int i=0;i<fileList.getLength();i++) {
                        Node file = fileList.item(i);
                        if (file.getNodeType() == Node.ELEMENT_NODE) {
                            Element fileElement = (Element) file;
                            collection.addFile(fileElement.getAttribute("name"), Paths.get(fileElement.getTextContent()));
                        }
                    }

                    project.getCorpus().addCollection( collection );
                }
			}

			// AUTHORS
            Node authorsNode = doc.getElementsByTagName("authors").item(0);
            NodeList authorsNodeChildren = authorsNode.getChildNodes();
            for (int temp = 0; temp < authorsNodeChildren.getLength(); temp++) {
                Node nodeCorpusChild = corpusNodeChildren.item(temp);
                if(nodeCorpusChild.getNodeName().equals("author")) {
                    Author author = new Author( nodeCorpusChild.getAttributes().getNamedItem("name").getTextContent() );
                    author.setAgeMin( Integer.valueOf(nodeCorpusChild.getAttributes().getNamedItem("ageMin").getTextContent()) );
                    author.setAgeMax( Integer.valueOf(nodeCorpusChild.getAttributes().getNamedItem("ageMax").getTextContent()) );
                    author.setGender( Integer.valueOf(nodeCorpusChild.getAttributes().getNamedItem("gender").getTextContent()) );
                    project.addAuthor( author );
                }
            }
			
		 } catch (Exception e) {
			 throw new Exception("Project file could not be read:" + e.getMessage());
	     }
		
		return project;
	}
	
	/**
	 * Writes a XML file containing lemma data.
	 * @param lemmaLibrary The file containing the save path in the file system.
	 * @param lemmaItems The list to serialize
	 * @throws Exception Thrown if XML file can't be created or saved.
	 */
	public static void writeLemmaList(File lemmaLibrary, List<LemmaLibraryItem> lemmaItems) throws Exception {

	    try {

	        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
	        DocumentBuilder build = dFact.newDocumentBuilder();
	        Document doc = build.newDocument();

	        Element root = doc.createElement("lemma_library");
	        doc.appendChild(root);

	        Element lemmasNode = doc.createElement("lemmas");
	        root.appendChild(lemmasNode);


	        for (LemmaLibraryItem dtl : lemmaItems) {
	            Element lemmaNode = doc.createElement("lemma");

	            Element nameNode = doc.createElement("name");
	            nameNode.appendChild(doc.createTextNode(String.valueOf(dtl.getName())));
	            lemmaNode.appendChild( nameNode );
	            
	            Element variationsNode = doc.createElement("variations");
	            for (String var : dtl.getVariations()) {
	            	 Element variationNode = doc.createElement("var");
	            	 variationNode.appendChild(doc.createTextNode(String.valueOf( var )));
	            	 variationsNode.appendChild(variationNode);
	            }
	            lemmaNode.appendChild( variationsNode );
	            
	            lemmasNode.appendChild(lemmaNode);
	        }

	        // Save the document to the disk file
	        TransformerFactory tranFactory = TransformerFactory.newInstance();
	        Transformer aTransformer = tranFactory.newTransformer();

	        // format the XML nicely
	        aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

	        aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        DOMSource source = new DOMSource(doc);
	        try {
	            // location and name of XML file you can change as per need
	            FileWriter fos = new FileWriter( lemmaLibrary );
	            StreamResult result = new StreamResult(fos);
	            aTransformer.transform(source, result);

	        } catch (IOException e) {

	            e.printStackTrace();
	        }

	    } catch (TransformerException e) {
	    	throw new Exception("Error outputting lemma library:" + e.getMessage());

	    } catch (ParserConfigurationException e) {
	    	throw new Exception("Error building lemma library:" + e.getMessage());
	    }
	}
	
	/**
	 * Reads a lemma list object from an XML file
	 * @param lemmaLibrary The file in the file system to read into the XML
	 * @return The parsed lemma list
	 * @throws Exception Thrown if file could not be opened or parsed.
	 */
	public static List<LemmaLibraryItem> readLemmaList(File lemmaLibrary) throws Exception {
		List<LemmaLibraryItem> list = new ArrayList<>();
		
		try {
			//File fXmlFile = new File("./library.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse( lemmaLibrary );

			//optional, but recommended
			doc.getDocumentElement().normalize();
			NodeList nLemmaList = doc.getElementsByTagName("lemma");

			for (int temp = 0; temp < nLemmaList.getLength(); temp++) {
				Node nLemmaNode = nLemmaList.item(temp);
				if (nLemmaNode.getNodeType() == Node.ELEMENT_NODE) {
					Element lemmaElement = (Element) nLemmaNode;
					
					String name = lemmaElement.getElementsByTagName("name").item(0).getTextContent();
					LemmaLibraryItem libraryItem = new LemmaLibraryItem(name);
					
					NodeList nVarList = lemmaElement.getElementsByTagName("var");
					for (int temp2 = 0; temp2 < nVarList.getLength(); temp2++) {
						Node nVarNode = nVarList.item(temp2);
						if (nVarNode.getNodeType() == Node.ELEMENT_NODE) {
							Element varElement = (Element) nVarNode;
							libraryItem.addVariation( varElement.getTextContent() );
						}
					}
					list.add( libraryItem );
				}
			}
			
		 } catch (Exception e) {
			throw new Exception("Lemma list could not be read:" + e.getMessage());
	     }
		
		return list;
	}
}
