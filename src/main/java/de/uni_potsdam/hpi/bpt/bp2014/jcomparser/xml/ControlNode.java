package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/***********************************************************************************
 *
 *   _________ _______  _        _______ _________ _        _______
 *   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 *      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 *      |  |  | (__    |   \ | || |         | |   |   \ | || (__
 *      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 *      |  |  | (      | | \   || | \_  )   | |   | | \   || (
 *   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 *   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 *******************************************************************
 *
 *   Copyright © All Rights Reserved 2014 - 2015
 *
 *   Please be aware of the License. You may found it in the root directory.
 *
 ************************************************************************************/
public class ControlNode implements IDeserialisation
{
    private String type;
    private int id;
    private String text;
    private boolean global;

    /**
     * Sets all its attributes from a given XML-Snippet
     * @param element The XML-Element
     */
    @Override
    public void initializeInstanceFromXML(Element element) {
        NodeList properties = element.getElementsByTagName("property");
        for (int i = 0; i < properties.getLength(); i++) {
            Node property = properties.item(i);
            initializeField(property);
        }
    }

    /**
     * If possible the field, which is described by the given property, is set.
     * @param property the describing property
     */
    private void initializeField(Node property) {
        NamedNodeMap attributes = property.getAttributes();
        String name = attributes.getNamedItem("name").getTextContent();
        String value = attributes.getNamedItem("value").getTextContent();
        switch (name) {
            case "#type" :
                type = value;
                break;
            case "#id" :
                id = Integer.parseInt(value);
                break;
            case "text" :
                text = value;
                break;
            case "global" :
                global = value.equals("1") ? true : false;
                break;
        }
    }

    /**
     * Writes the information of the object to the database
     * @return int - the autoincrement id of the newly created row
     */
    public int writeToDatabase() {
        return 0;
    }
}
