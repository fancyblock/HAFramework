/**
 * 
 */
package haframework.task;

import haframework.events.TouchEvent;
import haframework.task.conf.eTaskState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

/**
 * @author hjb
 *
 */
public class TaskManager
{
	//-------------------------------------------------- static member ----------------------------------------------------------
	
	static private TaskManager m_instance = null;
	static private boolean	m_safeFlag = false;
	
	/**
	 * @desc	return the singleton of TaskManager
	 * @return
	 */
	static public TaskManager Singleton()
	{
		if( m_instance == null )
		{
			m_safeFlag = true;
			m_instance = new TaskManager();
			m_safeFlag = false;
		}
		
		return m_instance;
	}

	//-------------------------------------------------- private member -----------------------------------------------------------
	
	private LinkedList<Task> m_taskList = null;
	private LinkedList<Task> m_pendingTasks = null;
	private LinkedList<Task> m_runningTasks = null;
	
	//-------------------------------------------------- public function ----------------------------------------------------------
	
	/**
	 * @desc	constructor
	 */
	public TaskManager()
	{
		if( m_safeFlag == false )
		{
			throw new Error( "[Error]: TaskManager is a singleton , can not be created directly !" );
		}
		
		// create the task container
		m_taskList = new LinkedList<Task>();
		m_pendingTasks = new LinkedList<Task>();
		m_runningTasks = new LinkedList<Task>();
	}
	
	/**
	 * @desc	quit 
	 */
	public void Quit()
	{
		this.RemoveAll();
		
//		m_taskList = null;
//		m_pendingTasks = null;
//		m_runningTasks = null;
	}
	
	/**
	 * @desc	add a task to the queue
	 * @param 	task
	 * @param 	priority
	 * @return
	 */
	public boolean Add( Task task, int priority )
	{
		if( task.GetStatus() != eTaskState.STATUS_IDLE )
		{
			//throw new Error( "[Error]: Can not add a task that status is not idle !" );
			
			return false;
		}
		
		task.SetStatus( eTaskState.STATUS_ADDING );
		task.SetPriority( priority );
		this.m_pendingTasks.push( task );
		
		return true;
	}
	
	/**
	 * @desc	remove a task from the queue
	 * @param	task
	 * @return
	 */
	public boolean Remove( Task task )
	{
		if( task.GetStatus() != eTaskState.STATUS_RUNNING )
		{
			return false;
		}
		
		task.SetStatus( eTaskState.STATUS_REMOVING );
		this.m_pendingTasks.push( task );
		
		return true;
	}
	
	/**
	 * @desc	remove all the tasks at the queue
	 */
	public void RemoveAll()
	{
		ListIterator<Task> taskIterator;
		Task task;
		
		taskIterator = m_runningTasks.listIterator();
		while( taskIterator.hasNext() == true )
		{
			task = taskIterator.next();
			task.vEnd();
		}

		m_runningTasks.clear();
		m_pendingTasks.clear();
		
		taskIterator = m_taskList.listIterator();
		while( taskIterator.hasNext() == true )
		{
			task = taskIterator.next();
			task.vDestroy();
		}
		m_taskList.clear();
		
	}
	
	/**
	 * @desc	logic frame
	 * @param	elepsed
	 */
	private ArrayList<DelayCallData> eraseList = new ArrayList<DelayCallData>();
	private ArrayList<ActionData> actEraseList = new ArrayList<ActionData>();
	public void Main( float elepsed )
	{
		Task task;
		DelayCallData delayData;
		ActionData actData;
		
		int len = m_runningTasks.size();
		for( int i = 0; i < len; i++ )
		{
			task = m_runningTasks.get(i);
			task.vMain( elepsed );
			
			int j;
			
			// process action
			actEraseList.clear();
			ArrayList<ActionData> actionList = task.GetActions();
			int actionLen = actionList.size();
			for( j = 0; j < actionLen; j++ )
			{
				actData = actionList.get(j);

				if( actData._forceStop == true )
				{
					actEraseList.add( actData );
				}
				else if( actData._action.vUpdate( elepsed ) == false )
				{
					actEraseList.add( actData );
				}
			}
			
			// erase delay call
			actionLen = actEraseList.size();
			for( j = 0; j < actionLen; j++ )
			{
				actData = actEraseList.get(j);
				
				actData._action.vLeave();
				
				// invoke the callback
				if( actData._actionCallback != null && actData._forceStop == false )
				{
					try
					{
						actData._actionCallback.invoke( task );
					}
					catch( Exception e )
					{
					}
				}
				actionList.remove( actData );
			}
			
			// process delay call
			eraseList.clear();
			
			ArrayList<DelayCallData> delayCallList = task.GetDelayCall();
			int delayCallLen = delayCallList.size();
			for( j = 0; j < delayCallLen; j++ )
			{
				delayData = delayCallList.get(j);
				delayData._delayTime -= elepsed;
					
				if( delayData._delayTime <= 0 )
				{
					try
					{
						delayData._delayCall.invoke( task );
					}
					catch( Exception e )
					{
					}
						
					eraseList.add( delayData );
				}
			}
			
			// erase delay call
			delayCallLen = eraseList.size();
			for( j = 0; j < delayCallLen; j++ )
			{
				delayData = eraseList.get(j);
				delayCallList.remove( delayData );
			}
		}
	}
	
	/**
	 * @desc	render
	 * @param	elepsed
	 */
	public void Draw( float elepsed )
	{
		Task task;
		
		int len = m_runningTasks.size();
		for( int i = 0; i < len; i++ )
		{
			task = m_runningTasks.get(i);
			task.vDraw( elepsed );
			
			// render action
			ArrayList<ActionData> actionList = task.GetActions();
			int actionLen = actionList.size();
			for( int j = 0; j < actionLen; j++ )
			{
				ActionData actData = actionList.get(j);
				actData._action.vDraw( elepsed );
			}
			
		}
	}
	
	/**
	 * @desc	process tasks in the pending list
	 */
	public void ProcessPending()
	{
		Task task;
		LinkedList<Task> tasks;
		
		int len = m_pendingTasks.size();
		
		for( int i = 0; i < len; i++ )
		{
			task = m_pendingTasks.get(i);
			
			int taskStatus = task.GetStatus();
			tasks = m_runningTasks;
			
			if( taskStatus == eTaskState.STATUS_ADDING )
			{
				tasks.add( task );
				task.SetStatus( eTaskState.STATUS_RUNNING );
				task.vBegin();
			}
			else if( taskStatus == eTaskState.STATUS_REMOVING )
			{
				if( tasks.contains( task ) == true )
				{
					tasks.remove( task );
					task.SetStatus( eTaskState.STATUS_IDLE );
					task.vEnd();
				}
				else
				{
					throw new Error( "[Error]: the removed task is not in task list !" );
				}
			}
			else
			{
				throw new Error( "[Error]: status of task in pending list must be Adding or Removing !" );
			}
		}
		
		m_pendingTasks.clear();
	}
	
	/**
	 * @desc	callback when event
	 * @param	events
	 */
	public void onTouchEvent( Vector<TouchEvent> events )
	{
		ListIterator<Task> taskIterator;
		Task task;
	
		LinkedList<Task> tasks = m_runningTasks;
		taskIterator = tasks.listIterator();
			
		while( taskIterator.hasNext() == true )
		{
			task = taskIterator.next();
				
			if( task.GetStatus() == eTaskState.STATUS_RUNNING )
			{
				if( task.vOnTouchEvent( events ) == true )
				{
					return;
				}
			}
		}
	}
	
	/**
	 * @desc	add a task when the task be created
	 * @param	task
	 */
	public void insertTask( Task task )
	{
		m_taskList.add( task );
	}

}
