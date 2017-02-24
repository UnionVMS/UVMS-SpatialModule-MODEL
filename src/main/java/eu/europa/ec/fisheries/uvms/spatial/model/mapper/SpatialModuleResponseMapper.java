/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.enums.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelValidationException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.List;

public final class SpatialModuleResponseMapper {

    final static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

    private SpatialModuleResponseMapper() {
    }

    private static void validateResponse(TextMessage response, String correlationId) throws SpatialModelValidationException {

        try {
            if (response == null) {
                throw new SpatialModelValidationException("Error when validating response in ResponseMapper: Response is Null");
            }

            if (response.getJMSCorrelationID() == null) {
                throw new SpatialModelValidationException("No correlationId in response (Null) . Expected was: " + correlationId);
            }

            if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
                throw new SpatialModelValidationException("Wrong correlationId in response. Expected was: " + correlationId + " But actual was: " + response.getJMSCorrelationID());
            }

            //the following code is catching the exception in purpose. DO NOT MODIFY it!
            try{
                SpatialFault fault = JAXBMarshaller.unmarshall(response, SpatialFault.class);
                throw new SpatialModelValidationException(fault.getCode() + " : " + fault.getFault());
            } catch (SpatialModelMarshallException e) {
                LOG.info("Expected Exception"); // Exception received in case if the validation is success
            }

        } catch (JMSException e) {
            LOG.error("JMS exception during validation ", e);
            throw new SpatialModelValidationException("JMS exception during validation " + e.getMessage());
        }
    }

    public static SpatialFault createFaultMessage(FaultCode code, String message) {
        SpatialFault fault = new SpatialFault();
        fault.setCode(code.getCode());
        fault.setFault(message);
        return fault;
    }

    public static String mapAreaByLocationResponse(final List<AreaExtendedIdentifierType> areasByLocation) throws SpatialModelMarshallException {
        try {
            AreaByLocationSpatialRS response = new AreaByLocationSpatialRS();
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            if (areasByLocation != null) {
                areasByLocationType.getAreas().addAll(areasByLocation);
            }
            response.setAreasByLocation(areasByLocationType);
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(areasByLocation, e);
        }
    }

    public static AreasByLocationType mapToAreasByLocationTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            AreaByLocationSpatialRS areaByLocationSpatialRS = JAXBMarshaller.unmarshall(response, AreaByLocationSpatialRS.class);
            return areaByLocationSpatialRS.getAreasByLocation();
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static String mapAreaTypeNamesResponse(final List<String> areaTypeNames) throws SpatialModelMarshallException {
        try {

            AreaTypeNamesSpatialRS response = new AreaTypeNamesSpatialRS();
            AreasNameType areasNameType = new AreasNameType();
            if (areaTypeNames != null) {
                for (String areaType: areaTypeNames) {
                    areasNameType.getAreaTypes().add(AreaType.fromValue(areaType));
                }
            }
            response.setAreaTypes(areasNameType);
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(areaTypeNames, e);
        }
    }

    public static AreasNameType mapToAreasNameTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            AreaTypeNamesSpatialRS areaTypeNamesSpatialRS = JAXBMarshaller.unmarshall(response, AreaTypeNamesSpatialRS.class);
            return areaTypeNamesSpatialRS.getAreaTypes();
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static String mapClosestLocationResponse(List<Location> closestLocations) throws SpatialModelMarshallException {
        try {
            ClosestLocationSpatialRS response = new ClosestLocationSpatialRS();
            ClosestLocationsType closestLocationsType = new ClosestLocationsType();
            if (closestLocations != null) {
                closestLocationsType.getClosestLocations().addAll(closestLocations);
            }
            response.setClosestLocations(closestLocationsType);
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(closestLocations, e);
        }
    }

    public static ClosestLocationsType mapToClosestLocationsTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            ClosestLocationSpatialRS closestLocationSpatialRS = JAXBMarshaller.unmarshall(response, ClosestLocationSpatialRS.class);
            return closestLocationSpatialRS.getClosestLocations();
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static String mapClosestAreaResponse(final List<Area> closestAreas) throws SpatialModelMarshallException {
        try {
            ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
            ClosestAreasType closestAreasType = new ClosestAreasType();
            if (closestAreas != null) {
                closestAreasType.getClosestAreas().addAll(closestAreas);
            }
            response.setClosestArea(closestAreasType);
            return JAXBMarshaller.marshall(response);
        } catch (SpatialModelMarshallException e) {
            return exception(closestAreas, e);
        }
    }

    public static ClosestAreasType mapToClosestAreasTypeFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            ClosestAreaSpatialRS closestAreaSpatialRS = JAXBMarshaller.unmarshall(response, ClosestAreaSpatialRS.class);
            return closestAreaSpatialRS.getClosestArea();
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static String mapEnrichmentResponse(final SpatialEnrichmentRS spatialEnrichmentRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(spatialEnrichmentRS);
        } catch (SpatialModelMarshallException e) {
            return exception(spatialEnrichmentRS, e);
        }
    }

    public static SpatialEnrichmentRS mapToSpatialEnrichmentRSFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, SpatialEnrichmentRS.class);
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static String mapFilterAreasResponse(FilterAreasSpatialRS filterAreasSpatialRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(filterAreasSpatialRS);
        } catch (SpatialModelMarshallException e) {
            return exception(filterAreasSpatialRS, e);
        }
    }

    public static String mapSpatialGetMapConfigurationResponse(SpatialGetMapConfigurationRS spatialGetMapConfigurationRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(spatialGetMapConfigurationRS);
        } catch (SpatialModelMarshallException e) {
            return exception(spatialGetMapConfigurationRS, e);
        }
    }

    public static String mapPingResponse(PingRS pingRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(pingRS);
        } catch (SpatialModelMarshallException e) {
            return exception(pingRS, e);
        }
    }

    public static FilterAreasSpatialRS mapToFilterAreasSpatialRSFromResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, FilterAreasSpatialRS.class);
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static String mapSpatialSaveOrUpdateMapConfigurationRSToString(SpatialSaveOrUpdateMapConfigurationRS spatialSaveOrUpdateMapConfigurationRS) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(spatialSaveOrUpdateMapConfigurationRS);
        } catch (SpatialModelMarshallException e) {
            return exception(spatialSaveOrUpdateMapConfigurationRS, e);
        }
    }


    public static String mapAreaByCodeResponseToString(AreaByCodeResponse areaByCodeResponse) throws SpatialModelMarshallException {
        try {
            return JAXBMarshaller.marshall(areaByCodeResponse);
        } catch (SpatialModelMarshallException e) {
            return exception(areaByCodeResponse, e);
        }
    }

    public static AreaByCodeResponse mapAreaByCodeResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, AreaByCodeResponse.class);
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static SpatialDeleteMapConfigurationRS mapToSpatialDeleteMapConfigurationRS(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, SpatialDeleteMapConfigurationRS.class);
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static SpatialSaveOrUpdateMapConfigurationRS mapToSpatialSaveOrUpdateMapConfigurationRS(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, SpatialSaveOrUpdateMapConfigurationRS.class);
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    public static SpatialGetMapConfigurationRS mapToSpatialGetMapConfigurationRS(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            return JAXBMarshaller.unmarshall(response, SpatialGetMapConfigurationRS.class);
        } catch (SpatialModelMarshallException e) {
            return exception(e);
        }
    }

    private static <T> String exception(T data, SpatialModelMarshallException e) throws SpatialModelMarshallException {
        LOG.error("[ Error when marshalling data. ] {}", e);
        throw new SpatialModelMarshallException("Error when marshalling " + data.getClass().getName() + " to String");
    }

    private static <T> T exception(SpatialModelMarshallException e) throws SpatialModelMarshallException {
        LOG.error("[ Error when marshalling data. ] {}", e);
        throw new SpatialModelMarshallException("Error when marshalling object to String", e);
    }

}