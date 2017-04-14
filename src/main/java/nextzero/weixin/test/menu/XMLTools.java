package nextzero.weixin.test.menu;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

public class XMLTools {

    // 多线程安全的Context.
    private JAXBContext jaxbContext;

    /**
     * @param types
     *            所有需要序列化的Root对象的类型.
     */
    public XMLTools(Class<?>... types) {
        try {
            jaxbContext = JAXBContext.newInstance(types);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Java Object->Xml.
     */
    public String toXml(Object root, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            createMarshaller(encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Java Object->Xml, 特别支持对Root Element是Collection的情形.
     */
    @SuppressWarnings("unchecked")
    public String toXml(Collection root, String rootName, String encoding) {
        try {
            CollectionWrapper wrapper = new CollectionWrapper();
            wrapper.collection = root;

            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(
                    new QName(rootName), CollectionWrapper.class, wrapper);

            StringWriter writer = new StringWriter();
            createMarshaller(encoding).marshal(wrapperElement, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Xml->Java Object.
     */
    @SuppressWarnings("unchecked")
    public <T> T fromXml(String xml) {
        try {
            StringReader reader = new StringReader(xml);
            return (T) createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Xml->Java Object, 支持大小写敏感或不敏感.
     */
    @SuppressWarnings("unchecked")
    public <T> T fromXml(String xml, boolean caseSensitive) {
        try {
            String fromXml = xml;
            if (!caseSensitive)
                fromXml = xml.toLowerCase();
            StringReader reader = new StringReader(fromXml);
            return (T) createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建Marshaller, 设定encoding(可为Null).
     */
    public Marshaller createMarshaller(String encoding) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (!encoding.trim().isEmpty()) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }
            return marshaller;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建UnMarshaller.
     */
    public Unmarshaller createUnmarshaller() {
        try {
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    //通过截字符串的方式去除xml的xsi(简单粗暴,不好)
    public static String cutXsi(String xml, Class<?>... types) {
        for (Class type : types) {
            String typeName = type.getSimpleName();
            String firstChar = typeName.charAt(0) + "";
            String xsiStr = String.format("xsi:type=\"%s\" ",
                    typeName.replaceFirst(firstChar, firstChar.toLowerCase()));
            xml = xml.replaceAll(xsiStr, "");
        }
        return xml.replaceAll(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    public static class CollectionWrapper {
        @XmlAnyElement
        protected Collection collection;
    }

    /*
     * 设置spring基础信息
     * @param path Spring的配置文件路径
     * @param pathValue mybatis的配置文件名称
     * @param ip 数据库IP地址
     * @param port 数据库端口地址
     * @param username 数据库用户名
     * @param password 数据库密码
     * @param base 连接的数据库名
    * */
    public static boolean setSpringConf(String path,String pathValue,String ip,String port,String username,String password,String base) {
        try {
            // 1.得到DOM解析器的工厂实例
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // 2.从DOM工厂里获取DOM解析器
            DocumentBuilder db = dbf.newDocumentBuilder();
            // 3.解析XML文档，得到document，即DOM树
            Document doc = db.parse(path);
            Element brandElement;
            String brandName;
            NodeList list=doc.getElementsByTagName("bean");
            NodeList tNodeList ;
            for(int i=0;i<list.getLength();i++){
                brandElement=(Element) list.item(i);
                brandName=brandElement.getAttribute("id");
                if(brandName.equals("dataSource")){
                    //属性修改
                    tNodeList = brandElement.getElementsByTagName("property");
                    for(int j=0;j<tNodeList.getLength();j++) {
                        brandElement=(Element) tNodeList.item(j);
                        brandName=brandElement.getAttribute("name");
                        if("jdbcUrl".equals(brandName)) {
                            String jdbcUrl_str = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&" +
                                    "characterEncoding=utf8&zeroDateTimeBehavior=convertToNull",ip,port,base);
                            brandElement.setAttribute("value", jdbcUrl_str);
                        }
                        if("user".equals(brandName)) {
                            brandElement.setAttribute("value", username);
                        }
                        if("password".equals(brandName)) {
                            brandElement.setAttribute("value", password);
                        }
                    }
                } else if(brandName.equals("sqlSessionFactory")) {
                    //属性修改
                    tNodeList = brandElement.getElementsByTagName("property");
                    for(int j=0;j<tNodeList.getLength();j++) {
                        brandElement=(Element) tNodeList.item(j);
                        brandName=brandElement.getAttribute("name");
                        if(brandName.equals("configLocation")) {
                            brandElement.setAttribute("value", pathValue);
                        }
                    }
                }
            }

            //保存xml文件
            TransformerFactory transformerFactory=TransformerFactory.newInstance();
            Transformer transformer=transformerFactory.newTransformer();
            DOMSource domSource=new DOMSource(doc);
            //设置编码类型
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
            StreamResult result=new StreamResult(new FileOutputStream(path));
            //把DOM树转换为xml文件
            transformer.transform(domSource, result);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

}
