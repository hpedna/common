package com.hpe.dna.common.mongodb;

import java.util.Date;
import java.util.List;

/**
 * @author chun-yang.wang@hpe.com
 */
@MongoCollectionAnnotation("dummy_entity")
public class DummyEntity extends Entity {
    private static final long serialVersionUID = -3001344476170232132L;
	private Date dateProperty;
    private Long longProperty;
    private Double doubleProperty;
    private Boolean booleanProperty;
    private Integer integerProperty;
    private String stringProperty;
    private List<String> listProperty;
    private byte[] binaryProperty;

    public Date getDateProperty() {
        return dateProperty;
    }

    public void setDateProperty(Date dateProperty) {
        this.dateProperty = dateProperty;
    }

    public Long getLongProperty() {
        return longProperty;
    }

    public void setLongProperty(Long longProperty) {
        this.longProperty = longProperty;
    }

    public Double getDoubleProperty() {
        return doubleProperty;
    }

    public void setDoubleProperty(Double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    public Boolean getBooleanProperty() {
        return booleanProperty;
    }

    public void setBooleanProperty(Boolean booleanProperty) {
        this.booleanProperty = booleanProperty;
    }

    public Integer getIntegerProperty() {
        return integerProperty;
    }

    public void setIntegerProperty(Integer integerProperty) {
        this.integerProperty = integerProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public void setListProperty(List<String> listProperty) {
        this.listProperty = listProperty;
    }

    public List<String> getListProperty() {
        return listProperty;
    }

    public byte[] getBinaryProperty() {
        return binaryProperty;
    }

    public void setBinaryProperty(byte[] binaryProperty) {
        this.binaryProperty = binaryProperty;
    }
}
