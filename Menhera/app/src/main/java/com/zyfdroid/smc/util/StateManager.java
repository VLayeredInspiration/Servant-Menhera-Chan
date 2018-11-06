package com.zyfdroid.smc.util;

public class StateManager
{
	private State currentState;
	
	public State getState(){
		callCalcState();
		return currentState;
	}
	
	private void callCalcState(){
		throw new NoSuchMethodError("not implement yet");
		
	}
	public void onDisturb(){
		throw new NoSuchMethodError("not implement yet");
	}
	
	
	
	public class State{
		public String notifyText;
		public int notifyImg;
		public String[] largeText;
		public int[] largeImg;
		public String leaveText;
		public int leaveImg;
		
		public String getNotifyText(){
			return notifyText;
		}
		public int getNotifyImg(){
			return notifyImg;
		}
		public String getLargeText(){
			return largeText
			[(int)(largeText.length*Math.random())];
		}
		public int getLargeImg(){
			return largeImg
				[(int)(largeImg.length*Math.random())];
		}
		
		public String getLeaveText(){
			return leaveText;
		}
		public int getLeaveImg(){
			return leaveImg;
		}
	}
	
}
