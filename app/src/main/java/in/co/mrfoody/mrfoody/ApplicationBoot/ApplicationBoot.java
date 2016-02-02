package in.co.mrfoody.mrfoody.ApplicationBoot;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by om on 24/1/16.
 */
public class ApplicationBoot implements KvmSerializable{
    public String SessionId;

    public ApplicationBoot(){}

    public ApplicationBoot(String sessionId){
        SessionId = sessionId;
    }

    @Override
    public Object getProperty(int index) {
        switch(index)
        {
            case 0:
                return SessionId;

        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 3;
    }

    @Override
    public void setProperty(int index, Object value) {
        switch(index)
        {
            case 0:
                SessionId = value.toString();
                break;
            default:
                break;
        }

    }

    @Override
    public void getPropertyInfo(int index, Hashtable properties, PropertyInfo info) {
        switch(index)
        {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "sessionId";
                break;

            default:break;
        }

    }
}
