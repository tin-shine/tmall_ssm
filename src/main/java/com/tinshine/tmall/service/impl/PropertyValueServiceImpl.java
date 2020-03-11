package com.tinshine.tmall.service.impl;

import com.tinshine.tmall.mapper.PropertyValueMapper;
import com.tinshine.tmall.pojo.Product;
import com.tinshine.tmall.pojo.Property;
import com.tinshine.tmall.pojo.PropertyValue;
import com.tinshine.tmall.pojo.PropertyValueExample;
import com.tinshine.tmall.service.PropertyService;
import com.tinshine.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    PropertyValueMapper propertyValueMapper;

    @Autowired
    PropertyService propertyService;

    @Override
    public void init(Product product) {
        List<Property> properties = propertyService.list(product.getCid());
        for (Property property: properties) {
            PropertyValue pValue = get(property.getId(), property.getId());
            if(pValue == null){
                pValue = new PropertyValue();
                pValue.setPid(product.getId());
                pValue.setPtid(property.getId());
                propertyValueMapper.insert(pValue);
            }
        }
    }

    @Override
    public void update(PropertyValue propertyValue) {
        propertyValueMapper.updateByPrimaryKeySelective(propertyValue);
    }

    @Override
    public PropertyValue get(int propertyId, int productId) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPtidEqualTo(propertyId).andPidEqualTo(productId);
        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(example);
        return propertyValues.isEmpty() ? null : propertyValues.get(0);
    }

    @Override
    public List<PropertyValue> list(int pid) {
        PropertyValueExample example = new PropertyValueExample();
        example.createCriteria().andPidEqualTo(pid);
        List<PropertyValue> propertyValues = propertyValueMapper.selectByExample(example);
        for (PropertyValue pValue : propertyValues) {
            pValue.setProperty(propertyService.get(pValue.getPid()));
        }
        return propertyValues;
    }
}