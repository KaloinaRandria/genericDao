package mg.working;

import mg.working.annotations.Column;
import mg.working.annotations.Table;
import mg.working.dao.Connect;
import mg.working.dao.GenericDAO;
import mg.working.test.Test;
import mg.working.test.Test2;
import mg.working.utility.DaoUtility;
import org.w3c.dom.ls.LSException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Test test = new Test("Kaloina","RANDRIA",22);
//        System.out.println(test.getClass().getAnnotation(Table.class).name());
//        System.out.println(test.getClass().getDeclaredField("nom").getAnnotation(Column.class).name());
//        System.out.println(DaoUtility.getTableName(test));

//            System.out.println(DaoUtility.getAllColumn(test)[1]);
//        System.out.println(DaoUtility.getListColumns(test));
//        System.out.println(DaoUtility.getAttributeValues(test)[0]);
//        System.out.println(DaoUtility.generateBaraingo(test));
        GenericDAO genericDAO = new GenericDAO();
//        genericDAO.save(null,test);
//        List<Object> testList = genericDAO.findAll(Connect.dbConnect(),test);
//        List<Object> testList = genericDAO.findCriteria(Connect.dbConnect(),test);
//        for (int i = 0; i < testList.size(); i++) {
//            System.out.println(((Test)testList.get(i)).getNom() +" "+ ((Test)testList.get(i)).getPrenom());
//
//        }
        List<Object> testList = genericDAO.findIntervalle(Connect.dbConnect(),test,20,40);
        for (int i = 0; i < testList.size(); i++) {
            System.out.println(((Test)testList.get(i)).getNom() +" "+ ((Test)testList.get(i)).getPrenom());
        }
//        List<Object[]> data = DaoUtility.traitementDonnees(Connect.dbConnect(),"Select * from t_test");
//        for (int i = 0; i < data.size(); i++) {
//            System.out.println(data.get(i).length);
//            for (int j = 0; j < data.get(i).length; j++) {
//                System.out.println(data.get(i)[j]);
//            }
//        }
//        List<Object> instances = DaoUtility.instanceClassWithConstructor("mg.working.test.Test",data);
//        Test2 test2 = new Test2("Rakoto");
//        genericDAO.save(null,test2);
    }
}