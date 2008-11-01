import java.util.ArrayList;
import java.util.List;


abstract class ChatUserSkel extends ChatUser implements ObjectImpl{
	
	public ChatUserSkel() {
		
	    Address addr = ORB.instance().address();
	    ObjectReference ior = new ObjectReference ("IDL:Account:1.0", addr);
	    objectReference (ior);

	    ORB.instance().registerObjectImpl(ior.stringify(),this);
	}
	
	public void invoke(ServerRequest req) {
		boolean a = dispatch(req);
		assert (a):"dispatch Error";
	}
	
	protected boolean dispatch(ServerRequest req) {
		
		/**
		 * TODO: implementar as operacoes
		 */
		if (req.opname().equals("notifyJoin")) {
			String name = req.getString();
			String ObjectReference = req.getReference();
			//echo("\n"+ObjectReference);
			ChatUser user = null;
			req.putStringReply("notifyJoin "+name+" received");
			notifyJoin(name, user);
			return true;
		}else if (req.opname().equals("notifyLeave")) {
			String name = req.getString();
			req.putStringReply("notifyLeave "+name+" received");
			notifyLeave(name);
			return true;
		}else if (req.opname().equals("notifyMessage")) {
			List sequence = req.getSequenceString();
			String sender = (String) sequence.get(0);
			String message = (String) sequence.get(1);
			notifyMessage(sender, message);
			return true;
		}else if (req.opname().equals("getUsername")) {
			String name = this.getUsername();
			//Adiciono o nome do usuario no reply
			req.putStringReply(name);
			
			return true;
		}
		
		return false;
	}
	
	private void echo(String msg){
		System.out.println("[ChatUserSkel] "+msg);
	}

}
