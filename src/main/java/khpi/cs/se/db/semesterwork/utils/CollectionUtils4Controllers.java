package khpi.cs.se.db.semesterwork.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils4Controllers {
    public static <E> List<List<E>> groupObjects(Iterable<E> list) {
        List<E> eList = (List<E>) list;
        List<List<E>> list4return = new ArrayList<List<E>>();
        int count = 0;
        for (int i = 0; i < eList.size(); i++) {
            list4return.add(new ArrayList<E>());
            list4return.get(count).add(eList.get(i++));
            if (i < eList.size()) {
                list4return.get(count).add(eList.get(i++));
            }
            if (i < eList.size()) {
                list4return.get(count++).add(eList.get(i));
            }
        }
        return list4return;

    }
}
