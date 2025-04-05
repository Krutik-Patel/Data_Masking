package com.backend.backend.utils.loader;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class XMLLoader implements DataLoaderStrategy {
    @Override
    public UnifiedHeirarchicalObject parseFile(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();

        Element rootElement = doc.getDocumentElement();
        UnifiedHeirarchicalObject root = convertElement(rootElement);
        return root;
    }

    private UnifiedHeirarchicalObject convertElement(Element element) {
        NodeList childNodes = element.getChildNodes();
        boolean hasElementChild = false;
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                hasElementChild = true;
                break;
            }
        }

        UnifiedHeirarchicalObject node = null;
        if (hasElementChild) {
            node = new UnifiedHeirarchicalObject(element.getTagName(), null);
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    UnifiedHeirarchicalObject childNode = convertElement((Element) child);
                    node.addChild(childNode);
                }
            }
        } else {
            String text = element.getTextContent().trim();
            node = new UnifiedHeirarchicalObject(element.getTagName(), text.isEmpty() ? null : text);
        }
        return node;
    }
}
