import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //CSV - JSON
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        //XML - JSON
        List<Employee> list1 = parseXML("data.xml");
        String xmlToJson = listToJson(list1);
        writeString(xmlToJson,"data1.json");

        //read JSON
        String json1 = readString("new_data.json");
        List<Employee> list2 = jsonToList(json1);
        for (Employee employee: list2) {
            System.out.println(employee);
        }
    }



    static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> mappingStrategy =
                    new ColumnPositionMappingStrategy<Employee>();
            mappingStrategy.setType(Employee.class);
            mappingStrategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(mappingStrategy)
                    .build();
            List<Employee> list = csv.parse();
            return list;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    static String listToJson(List<Employee> list) {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        Type listType = new TypeToken<List<Employee>>() {
        }
                .getType();
        return (gson.toJson(list, listType));
    }

    static void writeString(String json, String fileName) {

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<Employee>();

        try {
            Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new File(fileName));
            Node node = doc.getDocumentElement();
            NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                if (Node.ELEMENT_NODE == node_.getNodeType()) {
                    Element element= (Element) node_;
                    list.add(new Employee(Long.parseLong(element.getAttribute("id")) ,
                            element.getAttribute("firstName"),
                            element.getAttribute("lastName"),
                            element.getAttribute("Country"),
                            Integer.parseInt(element.getAttribute("age"))));
                }
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private static String readString(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            StringBuilder stringBuilder = new StringBuilder();
            String s;
            while ((s= reader.readLine())!= null){
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }


    private static List<Employee> jsonToList(String json) {
        JSONParser parser = new JSONParser();
        Gson gson = new GsonBuilder().create();
        List <Employee> list = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) parser.parse(json);
            for (int i = 0; i < array.size(); i++) {
                list.add(gson.fromJson(String.valueOf(array.get(i)), Employee.class));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
}