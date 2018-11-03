import java.util.List;
import java.util.LinkedList;
//import java.util.MainMP3;

public class ActionGesture<MainMP3> {
	List<MainMP3> list = new LinkedList<MainMP3>();
	int pos = 0;

	void add(MainMP3 s) {
		list.add(s);
	}

	void remove() {
		list.remove(list.size() - 1);
	}

	public action() {
		list.get(0).play();
		int pos = 0;
		boolean flag = true;
		while(true) {
			if(isDisturbed()) {
				if(disturbance == left) {
					list.get(pos).pause();
					pos--;
					if(pos<0)
						pos=list.size()-1;
				}

				else if(disturbance == right) {
					list.get(pos).pause();
					pos++;
					if(pos>=list.size())
						pos=0;
				}
				list.get(pos).play();
				else if(disturbance == both) {
					if(flag)
						list.get(pos).close();
					else
						list.get(pos).play();
				}
				if(list.get(pos).isComplete()) {
					pos++;
					if(pos>=list.size())
						pos=0;
					list.get(pos).play();
				}
			}
		}
	}
