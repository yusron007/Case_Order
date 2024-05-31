package com.casebtpn.Be_order.service;

import com.casebtpn.Be_order.model.OrderModel;
import com.casebtpn.Be_order.repository.OrderRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public byte[] generateOrderReport() throws Exception {
        List<OrderModel> orders = orderRepository.findAll();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);

        // Load template
        InputStream templateStream = resourceLoader.getResource("classpath:order.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "CaseBTPN");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

}
