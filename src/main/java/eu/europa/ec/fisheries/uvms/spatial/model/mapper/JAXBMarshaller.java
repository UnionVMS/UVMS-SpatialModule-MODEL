/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 **/
public class JAXBMarshaller {

    final static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

    private static Map<String, JAXBContext> contexts = new HashMap<>();

    /**
     * Marshalling a JAXB Object to a XML String representation
     *
     * @param <T> The type of data
     * @param data Data to be marshalled
     * @return
     * @throws eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException
     */
    public static <T> String marshallJaxBObjectToString(T data) throws SpatialModelMarshallException {
        try {
            JAXBContext jaxbContext = contexts.get(data.getClass().getName());
            if (jaxbContext == null) {
                long before = System.currentTimeMillis();
                jaxbContext = JAXBContext.newInstance(data.getClass());
                contexts.put(data.getClass().getName(), jaxbContext);
                LOG.debug("Stored contexts: {}", contexts.size());
                LOG.debug("JAXBContext creation time: {}", (System.currentTimeMillis() - before));
            }
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(data, sw);
            long before = System.currentTimeMillis();
            String marshalled = sw.toString();
            LOG.debug("StringWriter time: {}", (System.currentTimeMillis() - before));
            return marshalled;
        } catch (JAXBException ex) {
            LOG.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
    }

    /**
     * Unmarshalls a TextMessage to the desired Object. The object must be the
     * root object of the unmarshalled message!
     *
     * @param <R> The type to be unmarshalled to
     * @param textMessage The jsm TextMessage
     * @param clazz The class to be unmarshalled to
     * @return The unmarshalled instance of <R> 
     * @throws eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException
     */
    public static <R> R unmarshallTextMessage(TextMessage textMessage, Class<R> clazz) throws SpatialModelMarshallException {
        try {
            JAXBContext jc = contexts.get(clazz.getName());
            if (jc == null) {
                long before = System.currentTimeMillis();
                jc = JAXBContext.newInstance(clazz);
                contexts.put(clazz.getName(), jc);
                LOG.debug("Stored contexts: {}", contexts.size());
                LOG.debug("JAXBContext creation time: {}", (System.currentTimeMillis() - before));
            }
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(textMessage.getText());
            StreamSource source = new StreamSource(sr);
            long before = System.currentTimeMillis();
            R object = (R) unmarshaller.unmarshal(source);
            LOG.debug("Unmarshalling time: {}", (System.currentTimeMillis() - before));
            return object;
        } catch (NullPointerException | JMSException | JAXBException ex) {
            LOG.error("[ Error when marshalling Text message to object ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[Error when unmarshalling response in ResponseMapper ]", ex);
        }
    }

    /**
     * Unmarshalls A string message to the desired Object. The object must be the
     * root object of the unmarshalled message!
     *
     * @param <R> The type to be unmarshalled to
     * @param textMessage The jsm text in string
     * @param clazz The class to be unmarshalled to
     * @return The unmarshalled instance of <R> 
     * @throws eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException
     */
    public static <R> R unmarshallTextMessage(String textMessage, Class<R> clazz) throws SpatialModelMarshallException {
        try {
            JAXBContext jc = contexts.get(clazz.getName());
            if (jc == null) {
                long before = System.currentTimeMillis();
                jc = JAXBContext.newInstance(clazz);
                contexts.put(clazz.getName(), jc);
                LOG.debug("Stored contexts: {}", contexts.size());
                LOG.debug("JAXBContext creation time: {}", (System.currentTimeMillis() - before));
            }
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(textMessage);
            StreamSource source = new StreamSource(sr);
            long before = System.currentTimeMillis();
            R object = (R) unmarshaller.unmarshal(source);
            LOG.debug("Unmarshalling time: {}", (System.currentTimeMillis() - before));
            return object;
        } catch (NullPointerException | JAXBException ex) {
            LOG.error("[ Error when unmarshalling string text message to object ] {} ", ex.getMessage());
            throw new SpatialModelMarshallException("[Error when unmarshalling response in ResponseMapper ]", ex);
        }
    }

}