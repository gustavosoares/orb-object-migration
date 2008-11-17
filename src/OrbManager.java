import java.util.List;
import java.util.Map;


public abstract class OrbManager extends Object{
	abstract public boolean migrate(Map obj_impl);
	abstract public void list();
	abstract public void migrated();
}
