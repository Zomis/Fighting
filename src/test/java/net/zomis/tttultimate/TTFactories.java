package net.zomis.tttultimate;

public class TTFactories {
	private static final TTMNKParameters mnkEmpty = new TTMNKParameters(0, 0, 0);
	
	private static final TicFactory lastFactory = new TicFactory() {
		@Override
		public TTBase construct(TTBase parent, int x, int y) {
			return new TTBase(parent, x, y, mnkEmpty, null);
		}
	};
	private static final TicFactory areaFactory = new TicFactory() {
		@Override
		public TTBase construct(TTBase parent, int x, int y) {
			return new TTBase(parent, x, y, parent.getMNKParameters(), lastFactory);
		}
	};
	
	public static class Factory implements TicFactory {
		private final TTMNKParameters	mnk;
		private final TicFactory	next;
		public Factory(TTMNKParameters mnk, TicFactory nextFactory) {
			this.mnk = mnk;
			this.next = nextFactory;
		}
		@Override
		public TTBase construct(TTBase parent, int x, int y) {
			return new TTBase(parent, x, y, mnk, next);
		}
	}
	
	public TTBase classicMNK(int width, int height, int consecutive) {
		return new TTBase(null, new TTMNKParameters(width, height, consecutive), lastFactory);
	}
	public TTBase classicMNK(int mnk) {
		return classicMNK(mnk, mnk, mnk);
	}
	public TTBase ultimate(int mnk) {
		return ultimate(mnk, mnk, mnk);
	}
	public TTBase ultimate(int width, int height, int consecutive) {
		return new TTBase(null, new TTMNKParameters(width, height, consecutive), areaFactory);
	}
	public TTBase ultimate() {
		return ultimate(3);
	}
	public TTBase othello(int size) {
		return new TTBase(null, new TTMNKParameters(size, size, size + 1), lastFactory);
	}
	

}
