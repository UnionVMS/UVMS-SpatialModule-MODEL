/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.commons.xml.AbstractJAXBMarshaller;
import eu.europa.ec.fisheries.uvms.commons.xml.JAXBRuntimeException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.Map;

/**
 *
 **/
public class JAXBMarshaller extends AbstractJAXBMarshaller {

    final static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

    private static Map<String, JAXBContext> contexts = new HashMap<>();

    /**
     * Marshalling a JAXB Object to a XML String representation
     *
     * @param <T> The type of data
     * @param data Data to be marshalled
     * @return Marshalled string
     * @throws eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException If marshalling fails
     */
    public static <T> String marshallJaxBObjectToString(T data) throws SpatialModelMarshallException {
        try {
            Map<String,Object> properties = new HashMap<>();
            properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            return marshallToString(data, properties);
        } catch (JAXBException | JAXBRuntimeException ex) {
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
     * @throws eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException If unmarshalling fails
     */
    public static <R> R unmarshallTextMessage(TextMessage textMessage, Class<R> clazz) throws SpatialModelMarshallException {
        try {
            return unmarshallTextMessage(textMessage.getText(), clazz);
        } catch (NullPointerException | JMSException ex) {
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
     * @throws eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException If unmarshalling fails
     */
    public static <R> R unmarshallTextMessage(String textMessage, Class<R> clazz) throws SpatialModelMarshallException {
        try {
            return unmarshallTo(textMessage, clazz);
        } catch (NullPointerException | JAXBException | JAXBRuntimeException ex) {
            throw new SpatialModelMarshallException("[Error when unmarshalling response in ResponseMapper ]", ex);
        }
    }
}
