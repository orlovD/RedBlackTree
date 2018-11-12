import java.util.ArrayList;
import java.util.Collections;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for(int i=1;i<=10;i++){
			RBTree tree=new RBTree();
			int n=i*10000;
			ArrayList<Integer> list=new ArrayList<Integer>();
			for(int j=1;j<=n*10;j++)
				list.add(j);
			Collections.shuffle(list);
			int max=0;
			float maxM=0;
			int sum=0;
			int temp;
			int counter=0;
			for(int j=0;j<n;j++){
				temp=tree.insert(list.get(j));
				if(temp==2)
					counter++;
				sum+=temp;
				if((((float)sum)/((float)(j+1)))>maxM)
					maxM=((float)sum)/((float)(j+1));
				if(temp>max)
					max=temp;
			}
			System.out.println(maxM);
			
		}
	}

}
