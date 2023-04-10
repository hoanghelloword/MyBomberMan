package uet.oop.bomberman.entities;

/**
 * Entity có hiệu ứng hoạt hình
 */
public abstract class AnimatedEntitiy extends Entity {

	protected int _animate = 0;
	protected final int MAX_ANIMATE = 7500;
	//protected final int MAX_ANIMATE = 100;

	/**
	 * xem đã đủ animate chưa,
	 * chưa đủ thì tăng biến animate trong animatedEntity lên 1
	 * đã đủ thì set về 0
	 */
	protected void animate() {
		if(_animate < MAX_ANIMATE) _animate++;
		else _animate = 0;
	}

}
