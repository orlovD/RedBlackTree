import java.util.Stack;

/**
 * 
 * RBTree
 * 
 * An implementation of a Red Black Tree with
 * non-negative, distinct integer values
 * 
 */

public class RBTree {
	private int size=0;
	private RBNode root;
	private RBNode min;
	private RBNode max;
	
	 /**
	   * private boolean isRed(RBNode node)
	   * 
	   * returns true if node is red, false if black or null
	   *  
	   */
	private boolean isRed(RBNode node){
		if(node==null)
			return false;
		return node.red;
	}
	
  /**
   * public boolean empty()
   * 
   * returns true if and only if the tree is empty
   *  
   */
  public boolean empty() {
	  if(size==0)
		  return true;
    return false;
  }

 /**
   * public boolean search(int i)
   * 
   * returns i if the tree contains i
   *  otherwise, the closest value to i (in terms of absolute difference)
   */
  public int search(int i)
  {
	RBNode node=searchNode(i);
	if(node==null)
		return -1;
	return node.key;
  }
  /**
   * private RBNode searchNode(int i)
   * 
   * returns the RBNode that contain i if the tree contains i
   *  otherwise, the closest value to i (in terms of absolute difference) or null if tree is empty
   */
  private RBNode searchNode(int i){
	  if(size==0)//tree is empty
			return null;
		RBNode node=root;
		RBNode lastNode=root;
		while(node!=null){//searching for i
			if(node.key==i)
				return node;//i found
			if(node.key<i)
			{
				lastNode=node;
				node=node.Right;
			}
			else{
				lastNode=node;
				node=node.Left;
			}
		}
		int key1=lastNode.key;//i not found
		RBNode temp=lastNode;
		int difference=lastNode.key-i; 
		while(lastNode.father!=null&&(lastNode.father.key-lastNode.key)*difference>0){
			//searching for the closest to i
			difference=lastNode.father.key-lastNode.key;
			lastNode=lastNode.father;
		}
		if(lastNode.father==null){//return the closest
			return temp;
		}
		if(Math.abs(key1-i)<Math.abs(lastNode.father.key-i))
			return temp;
		return lastNode.father;
  }
	/**
	 * public void insert(int i)
	 * 
	 * inserts the integer i into the binary tree; the tree
	 * must remain valid (keep its invariants).
	 * the function returns the number of rotations, or 0 if i was already in the tree
	 * or no reotations were needed
	 */
	public int insert(int i) {
		//int numOfRotations=0;
		/*if(this.search(i)==i)//if value already in tree, do nothing
		{
			return 0;//no rotations
		}*/
		

		if(root==null)//if tree is empty add an item as root
		{
			root=new RBNode();//adding new node to root
			root.key=i;//adding key
			root.red=false;//painting black, no fix needed
			min=root;//only node- is the minimum and maximum;
			max=root;
			size++;
			return 0;//no rotations
		}

		RBNode node=root;//starting node is root
		RBNode newNode=new RBNode();//new node created
		newNode.key=i;//key added to a new node
		newNode.red=true;//new node color is red
		if(i<min.key)
			min=newNode;
		if(i>max.key)
			max=newNode;
		RBNode prevNode=root;

		while(node!=null)//search tree from root to leaf 
		{
			if(node.key>i)
			{
				prevNode=node;
				node=node.Left;
			}
			else if(node.key==i){
				return 0;
			}
			else
			{
				prevNode=node;
				node=node.Right;
			}
		}
		size++;
		node=newNode;//new node added
		if(prevNode.key>i)
			prevNode.Left=newNode;
		else
			prevNode.Right=newNode;
		newNode.father=prevNode;
		newNode.key=i;//key added to a new node
		newNode.red=true;//new node color is red

		return this.fixTree(newNode);

	}

	public int fixTree(RBNode newNode) {
		int rotations=0;
		RBNode uncleNode=new RBNode(); 
		while(newNode != root && newNode.father != null&&newNode.father.father!=null && newNode.father.red)
			//if father is black - no fix needed
		{
			
			if(newNode.father.father.Left==newNode.father)
			{
				uncleNode=newNode.father.father.Right;//uncle of new node
			}
			else
			{
				uncleNode=newNode.father.father.Left;//uncle of new node
			} 
			if(uncleNode!=null && uncleNode.red)//if uncle exists and he's red- case 1
			{
				newNode.father.red=false;//father of new node becomes black
				uncleNode.red=false;//uncle becomes black
				newNode.father.father.red=true;//grandfather becomes red
				newNode=newNode.father.father;//grandfather becomes new node
			}
			else
			{
				if(newNode==newNode.father.Right && newNode.father==newNode.father.father.Left)//case 2
				{
					rotate(false, newNode.father);//left rotation
					newNode=newNode.Left;//left child becomes new node
					rotations++;
				}
				else if(newNode==newNode.father.Left && newNode.father==newNode.father.father.Right)//case 2-other side
				{
					rotate(true, newNode.father);//right rotation
					newNode=newNode.Right;//right child becomes new node
					rotations++;
				}
				else//case 3
				{
					newNode.father.red=false;//father becomes black
					newNode.father.father.red=true;//grandfather becomes red
					if(newNode==newNode.father.Left)//if new node is left child
					{
						rotate(true,newNode.father.father);//right rotation of grandfather
						rotations++;
					}				   
					else//if new node is right child
					{
						rotate(false,newNode.father.father);//left rotation of grandfather
						rotations++;
					}
				}
			}

		}
		root.red=false;
		return rotations;
	}

  /**
   * public void delete(int i)
   * 
   * deletes the integer i from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * the function returns the number of rotations, or 0 if i was not in the tree
   * or no reotations were needed
   */
   public int delete(int i)
   {
	   int rotation=0;
	   RBNode node=searchNode(i);
	   if(node==null||node.key!=i)//key not found
		   return 0;
	   size--;
	   if(size==0){//i is the last node in the tree
		   root=null;
		   min=null;
		   max=null;
		   return 0;
	   }
	   if(node.key==min.key){
		   RBNode nodeMin=subTreeMin(node.Right);//delete min-check the possible new min
		   if(nodeMin!=null&&(node.father==null||nodeMin.key<node.father.key))
			   min=nodeMin;
		   else
			   min=node.father;
	   }
	   if(node.key==max.key){
		   RBNode nodeMax=subTreeMax(node.Left);//delete max-check the possible new max
		   if(nodeMax!=null&&(node.father==null||nodeMax.key>node.father.key))
			   max=nodeMax;
		   else
			   max=node.father;
	   }
	   if(node.Right!=null&&node.Left!=null){ //replace node with his successor
		   RBNode temp=subTreeMin(node.Right);
		   node.key=temp.key;
		   node=temp;
	   }
	   boolean red=node.red;
	   if(node==root){//node is the root and have one child
		   if(node.Right!=null)
			   root=node.Right;
		   else
			   root=node.Left;
		   root.father=null;
		   root.red=false;
		   return 0;
	   }
	   boolean right=(node.father.Right==node);//true if node is a right son
	   node=node.father;//delete node and connect node's left/right to father
	   if(node.getSon(right).Right!=null){
		   if(node.getSon(right).Right!=null)
			   node.getSon(right).Right.father=node;
		   node.setSon(right,node.getSon(right).Right);
		   
	   }else{
		   if(node.getSon(right).Left!=null)
			   node.getSon(right).Left.father=node;
		   node.setSon(right,node.getSon(right).Left);
		   
	   }
	   if(red) //red node deleted
		   return rotation;
	   if(node.getSon(right)!=null&&node.getSon(right).red){ //node son is red
		   node.getSon(right).red=false;
		   return rotation;
	   }
	   while(node!=null){//fix tree:
		   if(node.getSon(!right)!=null&&node.getSon(!right).red){//case 5
			   rotate(right,node);
			   rotation++;
			   node.red=true;
			   node.father.red=false;			   
		   }  
		   if(!node.getSon(!right).red&&node.red){
			   if(!isRed(node.getSon(!right).Right)&&!isRed(node.getSon(!right).Left)){
				   node.red=false;//case 2
				   node.getSon(!right).red=true;
				   return rotation;
			   }
		   }
		   if(!node.red&&!node.getSon(!right).red&&(!isRed(node.getSon(!right).Right))&&(!isRed(node.getSon(!right).Left))){
			   node.getSon(!right).red=true;//case 1
			   if(node.father!=null)
				   right=(node.father.Right==node);
			   node=node.father;
			   continue;
		   }
		   if(!node.getSon(!right).red&&isRed(node.getSon(!right).Left)||isRed(node.getSon(!right).Right)){
			   //case 3 and 4
			   if(!isRed(node.getSon(!right).getSon(!right))){
				   rotate(!right,node.getSon(!right));//case 3
				   rotation++;
				   node.getSon(!right).red=false;
				   node.getSon(!right).getSon(!right).red=true;
			   }
			   rotate(right,node);//case 4
			   rotation++;
			   node.father.red=node.red;
			   node.red=false;
			   if(node.father.getSon(!right)!=null)
				   node.father.getSon(!right).red=false;
			   return rotation;
		   }
		   
	   }
	   if(root.red)
		   root.red=false;
	   return rotation;
   }
 
   /**
    * public int min()
    * 
    * Returns the smallest key in the tree, or -1 if the tree is empty
    * is empty, returns -1;
    * 
    */
   public int min()
   {
	   if(min==null)
		   return -1;
	   return min.key;
	   
   }
   /**
    * private RBNode subTreeMin(RBNode node)
    * 
    * * Returns the RBNode with the smallest key in the sub tree that node is its root, or null if the tree is empty
    * is empty, returns -1;
    */
   private RBNode subTreeMin(RBNode node){
	   if(node==null)//tree is empty
		   return null;
	   while(node.Left!=null)//search for the minimum
		   node=node.Left;
	   return node;
   }
   
   /**
    * public int max()
    * 
    * Returns the largest key in the tree, or -1 if the tree is empty
    */
   public int max()
   {
	   if(max==null)
		   return -1; // tree is empty
	   return max.key;
   }

   /**
    * private RBNode subTreeMax(RBNode node)
    * 
    * * Returns the RBNode with the largest key in the sub tree that node is its root, or null if the tree is empty
    * is empty, returns -1;
    */
   private RBNode subTreeMax(RBNode node){
	   if(node==null)//tree is empty
		   return null;
	   while(node.Right!=null)//search for the maximum
		   node=node.Right;
	   return node;
   }
  /**
   * public void arrayToTree(int[] )
   * 
   * inserts the array of integers to the tree.
   * if the tree contained elements before, they should be discarded
   * the array contains integers in ascending order.
   * 
   */
  public void arrayToTree(int[] aa )
  {
	 int blackHigh=(int)Math.floor(Math.log10(aa.length+1)/(Math.log10(2)));
	 root= makeTree(aa,0,aa.length-1,blackHigh);
	 size=aa.length;
  }
  /**
   * RBNode makeTree(int[] array,int start,int end,int blackHigh)
   * 
   * recursive function for build up a tree from array
   * the array contains integers in ascending order.
   * 
   */
  private RBNode makeTree(int[] array,int start,int end,int blackHigh){
	  if(start>end)
		  return null;
	  RBNode node=new RBNode();
	  node.key=array[(start+end)/2];//root of the tree is the key in the middle
	  node.Left=makeTree(array,start,(start+end)/2-1,blackHigh-1);//make Left sub-tree
	  node.Right=makeTree(array,(start+end)/2+1,end,blackHigh-1);//make right sub-tree
	  if((start+end)/2==0)//key is the minimum
		  min=node;
	  if((start+end)/2==array.length-1)//key is the maximum
		  max=node;
	  if(node.Left!=null)
		  node.Left.father=node;
	  if(node.Right!=null)
		  node.Right.father=node;
	  if(blackHigh<=0)//painting red if need
		  node.red=true;
	  else
		  node.red=false;
	  return node;
  }
   /**
    * public int size()
    * 
    * Returns the number of nodes in the tree.
    * 
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return size;
   }
   /**
    * private void rotate(boolean right,RBNode node)
    * 
    *make rotation right if boolean is true or left if false
    */
  private void rotate(boolean right,RBNode node) {
	  if(right)
		  rotateRight(node);
	  else
		  rotateLeft(node);
  }
  /**
   * private void rotateRight(RBNode node)
   * 
   *make right rotation
   */
private void rotateRight(RBNode node){
	   RBNode temp=node.Left;
	   RBNode temp1=node.Left.Right;
	   node.Left=temp1;
	   temp.Right=node;
	   temp.father=node.father;
	   if(node.father!=null)
		   node.father.setSon(node.father.Right==node,temp);
	   else
		   root=temp;
	   node.father=temp;
	   if(temp1!=null)
		   temp1.father=node;
   }
/**
 * private void rotateRight(RBNode node)
 * 
 *make left rotation
 */
   private void rotateLeft(RBNode node){
	   RBNode temp=node.Right;
	   RBNode temp1=node.Right.Left;
	   node.Right=temp1;
	   temp.Left=node;
	   temp.father=node.father;
	   if(node.father!=null)
		   node.father.setSon(node.father.Right==node,temp);
	   else
		   root=temp;
	   node.father=temp;
	   if(temp1!=null)
		   temp1.father=node;
		   
   }
  /**
   * public class RBNode
   * 
   * If you wish to implement classes other than RBTree
   * (for example RBNode), do it in this file, not in 
   * another file 
   *  
   */
  private class RBNode{
	  public RBNode father;
	  public RBNode Left;
	  public RBNode Right;
	  public int key;
	  public boolean red;
	  /**
	   *public RBNode getSon(boolean right)
	   * 
	   *return the right son of node if boolean is true, or left if false
	   */
	  public RBNode getSon(boolean right){
		  if(right)
			  return Right;
		  return Left;
	  }
	  /**
	   *public RBNode getSon(boolean right)
	   * 
	   *set the right son of this to be node if boolean is true, or left son if false
	   */
	  public void setSon(boolean right,RBNode node){
		  if(right)
			  Right=node;
		  else
			  Left=node;
	  }
  }
  
  /**
 * @original author Shai Vardi
 * Modified for semester 2013 b
 */

}
  