package com.backend.backend.engine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.backend.backend.masks.MaskingStrategy;

public class ConfigLoaderTest {

    private ConfigLoader configLoader;

    @BeforeEach
    public void setUp() {
        configLoader = new ConfigLoader();
    }

    @Test
    public void testParseConfig() throws Exception {
        // Mock XML content for the config file
        String xmlContent = """
                    <fields_info>
                        <field>
                            <field_xPath>/data/field1</field_xPath>
                            <refersTo></refersTo>
                            <morphing_method>
                                <method_name>REDACT</method_name>
                                <method_parameters>
                                    <param1>value1</param1>
                                </method_parameters>
                            </morphing_method>
                        </field>
                        <full_data>
                            <algorithm>KANONYMIZATION</algorithm>
                            <identifiers>
                                <quasi_identifiers>
                                    <id_xPath>/data/field2</id_xPath>
                                </quasi_identifiers>
                                <sensitive_identifiers>
                                    <id_xPath>/data/field3</id_xPath>
                                </sensitive_identifiers>
                            </identifiers>
                            <parameters>
                                <k>3</k>
                            </parameters>
                        </full_data>
                    </fields_info>
                """;

        // Mock MultipartFile
        MockMultipartFile mockFile = new MockMultipartFile("config", "config.xml", "text/xml", xmlContent.getBytes());

        // Parse the config
        configLoader.parse(mockFile);

        // Verify maskStrategyMap
        Map<String, MaskingStrategy> maskStrategyMap = configLoader.getMaskStrategyMap();
        assertNotNull(maskStrategyMap);
        assertTrue(maskStrategyMap.containsKey("/data/field1"));
        assertEquals("RedactionMaskingStrategy", maskStrategyMap.get("/data/field1").getClass().getSimpleName());

        // Verify fulldataMasksList
        List<MaskingStrategy> fullDataMaskList = configLoader.getFullDataMaskStrategyList();
        assertNotNull(fullDataMaskList);
        assertEquals(1, fullDataMaskList.size());
        assertEquals("KAnonymizationMaskingStrategy", fullDataMaskList.get(0).getClass().getSimpleName());
        assertEquals("3", fullDataMaskList.get(0).getParameters().get("k"));
        assertNotEquals(0,
                ((List<String>) fullDataMaskList.get(0).getParameters().get("sensitive_identifiers")).size());
    }

    @Test
    public void testStringifyConfig() throws Exception {
        // Mock XML content for the config file
        String xmlContent = """
                    <fields_info>
                        <field>
                            <field_xPath>/data/field1</field_xPath>
                            <refersTo></refersTo>
                            <morphing_method>
                                <method_name>REDACT</method_name>
                                <method_parameters>
                                    <param1>value1</param1>
                                </method_parameters>
                            </morphing_method>
                        </field>
                    </fields_info>
                """;

        // Mock MultipartFile
        MockMultipartFile mockFile = new MockMultipartFile("config", "config.xml", "text/xml", xmlContent.getBytes());

        // Parse the config
        configLoader.parse(mockFile);

        // Verify stringifyConfig
        String configString = configLoader.stringifyConfig();
        assertNotNull(configString);
        assertTrue(configString.contains("<field_xPath>/data/field1</field_xPath>"));
        assertTrue(configString.contains("<method_name>REDACT</method_name>"));
    }

    @Test
    public void testForeignKeyMapPopulation() throws Exception {
        // Mock XML content with refersTo
        String xmlContent = """
                    <fields_info>
                        <field>
                            <field_xPath>/data/employee/dep_id</field_xPath>
                            <morphing_method>
                                <method_name>REDACT</method_name>
                                <method_parameters>
                                    <param1>value1</param1>
                                </method_parameters>
                            </morphing_method>
                            <refersTo>
                                <ref>/data/department/dep_id</ref>
                            </refersTo>
                        </field>
                    </fields_info>
                """;

        // Mock MultipartFile
        MockMultipartFile mockFile = new MockMultipartFile("config", "config.xml", "text/xml", xmlContent.getBytes());

        // Parse config
        configLoader.parse(mockFile);

        // Get and verify foreignKeyMap
        Map<String, List<String>> foreignKeyMap = configLoader.getForeignKeyMap();
        assertNotNull(foreignKeyMap);
        assertTrue(foreignKeyMap.containsKey("/data/department/dep_id"));

        List<String> referringFields = foreignKeyMap.get("/data/department/dep_id");
        assertNotNull(referringFields);
        assertTrue(referringFields.contains("/data/employee/dep_id"));
        assertEquals(1, referringFields.size());
    }

}