package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.message.AbstractJAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import lombok.extern.slf4j.Slf4j;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

@Slf4j
public class SpatialJAXBMarshaller extends AbstractJAXBMarshaller {

    /**
     * Marshalls a JAXB Object to a XML String representation
     *
     * @param <T>
     * @param data
     * @return
     * @throws SpatialModelMarshallException
     */
    public <T> String marshall(T data) throws SpatialModelMarshallException {
        try {
            return marshallJaxBObjectToString(data);
        } catch (JAXBException e) {
            log.error("[ Error when marshalling data. ] {}", e.getMessage());
            throw new SpatialModelMarshallException("Error when marshalling " + data.getClass().getName() + " to String");
        }
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarshalled message!
     *
     * @param <R>
     * @param textMessage
     * @param clazz clazz
     * @return
     * @throws SpatialModelMarshallException
     */
    public <R> R unmarshall(TextMessage textMessage, Class clazz) throws SpatialModelMarshallException {
        try {
            return unmarshallTextMessage(textMessage, clazz);
        } catch (JMSException | JAXBException e) {
            log.error("[ Error when unmarshalling data. ] {}", e.getMessage());
            throw new SpatialModelMarshallException("Error when unmarshalling response in ResponseMapper: " + e.getMessage());
        }
    }

}
