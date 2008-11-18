import java.util.List;
import java.util.Map;


public abstract class OrbManager extends Object{
	abstract public void list();
	abstract public void migrated();
	abstract boolean migrate(XmlMapper xmlmapper) ;
}
