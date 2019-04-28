package android.lifeistech.com.memo;

import io.realm.RealmObject;

public class Task extends RealmObject {

    public String title;
    public String updateDate;
    public String content;
    public boolean isCompleted;
    public String dateDeadline;
    public String timeDeadline;

}
