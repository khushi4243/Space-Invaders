import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Random;

import javalib.worldimages.*;
import javalib.funworld.*;
import java.awt.Color;

import tester.Tester;

// represents a list of type T
interface IList<T> {

  // filter this IList using the given predicate
  IList<T> filter(Predicate<T> pred);

  // map the given function onto every member of this IList
  <U> IList<U> map(Function<T, U> converter);

  // combine the items in this IList using the given function
  <U> U fold(BiFunction<T, U, U> converter, U initial);

  // checks to see if any of the items in the list return true after the predicate
  // is applied
  boolean ormap(Predicate<T> pred);

  // checks to see if all of the items in the list return true after the predicate
  // is applied
  boolean andmap(Predicate<T> pred);
  
  // retrieves the nth element from this list
  T getNth(int n);

  // determines the size of this list
  int sizeOfList();

}

//represents an empty list of type T
class MtList<T> implements IList<T> {

  MtList() {}

  /*
   * TEMPLATE
   * METHODS 
   * ... this.filter(Predicate) ...       -- IList<T>
   * ... this.map(Function) ...           -- IList<T>
   * ... this.fold(BiFunction, U) ...     -- U
   * ... this.ormap(Predicate) ...        -- boolean
   * ... this.andmap(Predicate) ...       -- boolean
   * ... this.getNth(int) ...             -- T
   * ... this.sizeOfList() ...            -- int
   */

  // filter this MtList using the given predicate
  public IList<T> filter(Predicate<T> pred) {
    return new MtList<T>();
  }

  // map the given function onto every member of this MtList
  public <U> IList<U> map(Function<T, U> converter) {
    return new MtList<U>();
  }

  // combine the items in this MtList using the given function
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return initial;
  }

  // checks to see if any of the items in the list return true after the predicate
  // is applied
  public boolean ormap(Predicate<T> pred) {
    return false;
  }

  // checks to see if all of the items in the list return true after the predicate
  // is applied
  public boolean andmap(Predicate<T> pred) {
    return true;
  }

  // throws an exception since you cannot retrieve the nth element of an empty list
  public T getNth(int n) {
    throw new IndexOutOfBoundsException("index is too high");
  }

  // determines the size of this empty list
  public int sizeOfList() {
    return 0;
  }

}

//represents a non-empty list of type T
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  // the constructor
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE
   * FIELDS
   * ... this.first ...                   -- T
   * ... this.rest ...                    -- IList<T>
   * METHODS 
   * ... this.filter(Predicate) ...       -- IList<T>
   * ... this.map(Function) ...           -- IList<T>
   * ... this.fold(BiFunction, U) ...     -- U
   * ... this.ormap(Predicate) ...        -- boolean
   * ... this.andmap(Predicate) ...       -- boolean
   * ... this.getNth(int) ...             -- T
   * ... this.sizeOfList() ...            -- int
   * METHODS FOR FIELDS
   * ... this.rest.filter(Predicate) ...       -- IList<T>
   * ... this.rest.map(Function) ...           -- IList<T>
   * ... Function.apply(T) ...                 -- T
   * ... this.rest.fold(BiFunction, U) ...     -- U
   * ... BiFunction.apply(U, U) ...            -- U
   * ... this.rest.ormap(Predicate) ...        -- boolean
   * ... this.rest.andmap(Predicate) ...       -- boolean
   * ... this.rest.getNth(int) ...             -- T
   * ... this.rest.sizeOfList() ...            -- int
   */

  // filters this ConsList<T> using the given predicate
  public IList<T> filter(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  // map the given function onto every member of this ConsList
  public <U> IList<U> map(Function<T, U> converter) {
    return new ConsList<U>(converter.apply(this.first), this.rest.map(converter));
  }

  // combine the items in this ConsList using the given function
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return converter.apply(this.first, this.rest.fold(converter, initial));
  }

  // checks to see if any of the items in the list return true after the predicate
  // is applied
  public boolean ormap(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return true;
    }
    else {
      return this.rest.ormap(pred);
    }
  }

  // checks to see if all of the items in the list return true after the predicate
  // is applied
  public boolean andmap(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return this.rest.andmap(pred);
    }
    else {
      return false;
    }

  }

  //retrieves the nth element from this list
  public T getNth(int n) {
    if (n == 0) {
      return this.first;
    }
    else {
      return this.rest.getNth(n - 1);
    }
  }

  //determines the size of this empty list
  public int sizeOfList() {
    return 1 + this.rest.sizeOfList();
  }

}

// stores extra methods used throughout the program
class Utils {
  Utils() {}

  /*
   * TEMPLATE
   * METHODS 
   * ... this.buildList(int, Function) ... -- IList<T>
   * METHODS FOR FIELDS 
   * ... this.buildList(int, Function) ... -- IList<T> 
   */

  // constructs a list with a size of the given number by applying the given
  // function to all integer indices
  public <T> IList<T> buildList(int num, Function<Integer, T> func) {
    if (num == 0) {
      return new MtList<T>();
    }
    else {
      return new ConsList<T>(func.apply(num), this.buildList(num - 1, func));
    }
  }
}

// represents the MovePieces function object used in map
class MovePieces implements Function<APiece, APiece> {

  MovePieces() {}

  /*
   * TEMPLATE
   * METHODS
   * ... this.apply(APiece) ...      -- APiece
   */
  
  // moves the given APiece according to its respective move() function
  public APiece apply(APiece given) {
    return given.move();
  }
}

// represents the IsNotCollidesList predicate object used in filter
class IsNotCollidesList implements Predicate<APiece> {
  IList<APiece> bulletsList;

  // the constructor
  IsNotCollidesList(IList<APiece> bulletsList) {
    this.bulletsList = bulletsList;
  }

  /*
   * TEMPLATE
   * FIELDS
   * ... this.bulletsList ...                       -- IList<APiece>
   * METHODS
   * ... this.test(APiece) ...                      -- boolean
   * METHODS FOR FIELDS
   * ... this.bulletsList.andmap(Predicate) ...     -- boolean
   */
  
  // determines if all of the bullets in this bulletsList do not collide with the given APiece
  public boolean test(APiece thisOne) {
    return this.bulletsList.andmap(new IsNotCollides(thisOne));
  }
}

// represents the IsNotCollides predicate object used in filter
class IsNotCollides implements Predicate<APiece> {
  APiece thisOne;

  // the constructor
  IsNotCollides(APiece thisOne) {
    this.thisOne = thisOne;
  }

  /*
   * TEMPLATE
   * FIELDS
   * ... this.thisOne ...                           -- APiece
   * METHODS
   * ... this.test(APiece) ...                      -- boolean
   * METHODS FOR FIELDS
   * ... this.thisOne.collides(APiece) ...          -- boolean
   */
  
  // determines if the APiece thisOne does not collide with the given APiece
  public boolean test(APiece other) {
    return !this.thisOne.collides(other);
  }

}

// represents the TestAndOrMapString predicate object used to test andmap/ormap using strings
class TestAndOrMapString implements Predicate<String> {
  TestAndOrMapString() {}

  /*
   * TEMPLATE 
   * METHODS 
   * ... this.test(String) ...             -- boolean 
   * METHODS FOR FIELDS 
   * ... String.startsWith(String) ...     -- boolean
   */

  // checks to see if the string starts with a
  public boolean test(String s) {
    return s.startsWith("a");
  }
}

//represents the TestAndOrMapInt predicate object used to test andmap/ormap using integers
class TestAndOrMapInt implements Predicate<Integer> {
  TestAndOrMapInt() {}

  /*
   * TEMPLATE 
   * METHODS
   * ... this.test(Integer) ...      -- Integer
   */

  // checks to see if the number is even
  public boolean test(Integer n) {
    return (n % 2 == 0);
  }
}

// represents the TurnIntoEnemyRows function object used with buildList
class TurnIntoEnemyRows implements Function<Integer, APiece> {
  int max;

  // the constructor
  TurnIntoEnemyRows(int max) {
    this.max = max;
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.max ...                 -- int 
   * METHODS 
   * ... this.apply(Integer) ...      -- Enemy
   */

  // makes a new Enemy with x and y coordinates created based on the given Integer
  public APiece apply(Integer i) {
    int x = ((this.max - i) % 9) * 60 + 30;
    int y = ((this.max - i) / 9) * 60 + 30;
    return new Enemy(x, y);
  }

}

// represents the  DrawPiece bifunction object used with fold
class DrawPiece implements BiFunction<APiece, WorldScene, WorldScene> {
  DrawPiece() {}

  /*
   * TEMPLATE 
   * METHODS 
   * ... this.apply(APiece, WorldScene) ...    -- WorldScene 
   * METHODS FOR FIELDS 
   * ... APiece.drawOnto(WorldScene) ...       -- WorldScene
   */

  // draws the given APiece by placing the image of the APiece onto the given
  // WorldScene accumulator in its respective x and y coordinates
  public WorldScene apply(APiece p, WorldScene acc) {
    return p.drawOnto(acc);
  }
}

// represents the EqualCartPt predicate object used in ormap
class EqualCartPt implements Predicate<CartPt> {
  CartPt cp;

  // the constructor
  EqualCartPt(CartPt cp) {
    this.cp = cp;
  }

  /*
   * TEMPLATE FIELDS 
   * ... this.cp ...                      -- CartPt 
   * METHODS 
   * ... this.test(CartPt) ...            -- boolean 
   * METHODS FOR FIELDS 
   * ... this.cp.equalsCartPt(CartPt) ... -- boolean
   */

  // determines if the given cartesian point is equal to this.cp cartesian point
  public boolean test(CartPt given) {
    return this.cp.equalsCartPt(given);
  }
}

// represents the Contains predicate object
class Contains implements Predicate<CartPt> {
  IList<CartPt> points;

  // the constructor
  Contains(IList<CartPt> points) {
    this.points = points;
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.points ...                              -- IList<CartPt> 
   * METHODS ...
   * ... this.test(CartPt) ...                        -- boolean 
   * METHODS FOR FIELDS ...
   * ... this.points.ormap(Predicate) ...             -- boolean
   */

  // determines if the given cartesian point is equal to one of the
  // points in this.points (which is a list of cartesian points)
  public boolean test(CartPt cp) {
    return this.points.ormap(new EqualCartPt(cp));
  }
}

// represents the IsOnScreen predicate object used with filter
class IsOnScreen implements Predicate<APiece> {
  int maxX;
  int maxY;

  // the constructor
  IsOnScreen(int maxX, int maxY) {
    this.maxX = maxX;
    this.maxY = maxY;
  }
  
  /*
   * TEMPLATE
   * FIELDS
   * ... this.maxX ...                    -- int
   * ... this.maxY ...                    -- int
   * METHODS
   * ... this.test(APiece) ...            -- boolean
   * METHODS FOR FIELDS
   * ... APiece.isOnScreen(int, int) ...  -- boolean
   */
  
  // determines if the location of the given APiece lies
  // within the bounds created by this.maxX and this.maxY
  public boolean test(APiece p) {
    return p.isOnScreen(maxX, maxY);
  }

}

// represents a Cartesian Point with x and y coordinates
class CartPt {
  int x;
  int y;

  // the constructor
  CartPt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.x ...                                          -- int 
   * ... this.y ...                                          -- int 
   * METHODS ...
   * ... this.moveCartPt(int, int) ...                       -- CartPt 
   * ... this.draw(APiece, WorldScene) ...                   -- WorldScene 
   * ... this.equalsCartPt(CartPt) ...                       -- boolean 
   * ... this.setWithinBounds(int, int, int, int) ...        -- CartPt
   * ... this.checkWithinBounds(int, int, int, int) ...      -- boolean
   * ... this.checkWithinCartBounds(CartPt, int, int, int)   -- boolean
   * METHODS FOR FIELDS
   * ... WorldScene.placeImageXY(WorldImage, int, int) ...   -- WorldScene
   * ... APiece.draw() ...                                   -- WorldScene
   */

  // moves the cartesian point by changing its x and y according to the given integers
  CartPt moveCartPt(int moveX, int moveY) {
    return new CartPt(this.x + moveX, this.y + moveY);

  }

  // draws the image of the given APiece onto the given WorldScene
  // using the x and y coordinates from this cartesian point
  WorldScene draw(APiece p, WorldScene w) {
    return w.placeImageXY(p.draw(), this.x, this.y);
  }

  // determines if this cartesian point equals the given cartesian point
  boolean equalsCartPt(CartPt given) {
    return this.x == given.x && this.y == given.y;
  }

  // produces a new cartesian point that stays within the given bounds
  CartPt setWithinBounds(int minX, int minY, int maxX, int maxY) {
    return new CartPt(Math.max(Math.min(this.x, maxX), minX),
        Math.max(Math.min(this.y, maxY), minY));
  }

  // determines if the x and y coordinates of this cartesian point fall within the given bounds
  boolean checkWithinBounds(int minX, int minY, int maxX, int maxY) {
    return (this.x <= maxX && this.x >= minX && this.y <= maxY && this.y >= minY);
  }

  // determines if there is a collision between circle & rectangle where given CartPt is circle
  // location, & this CartPt is rectangle location + other ints represent dimensions of rectangle
  boolean checkWithinCartBounds(CartPt circle, int width, int height, int radius) {

    // local variables that set the edges for testing
    int testX = circle.x;
    int testY = circle.y;
    
    if (circle.x < this.x - width / 2) {
      testX = this.x - width / 2;
    }
    else if (circle.x > this.x + width / 2) {
      testX = this.x + width / 2;
    }
    
    
    if (circle.y < this.y - height / 2) {
      testY = this.y - height / 2;
    }
    else if (circle.y > this.y + height / 2) {
      testY = this.y + height / 2;
    }

    // local variables that set the distances from the closest edges
    double distX = circle.x - testX;
    double distY = circle.y - testY;
    double distance = Math.hypot(distX, distY);

    // there is a collision if we find that distance is <= radius
    return distance <= radius;
  }

}

//represents the abstract class APiece
abstract class APiece {
  CartPt location;
  int dx;    // speed along x-axis
  int dy;    // speed along y-axis
  boolean isOffScreen;

  // the constructor
  APiece(CartPt location, int dx, int dy, boolean isOffScreen) {
    this.location = location;
    this.dx = dx;
    this.dy = dy;
    this.isOffScreen = isOffScreen;
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.location ...                   -- CartPt 
   * ... this.dx ...                          - int 
   * ... this.dy ...                          - int 
   * ... this.isOnScreen ...                  - boolean
   * METHODS 
   * ... this.draw() ...                     -- WorldImage 
   * ... this.move() ...                     -- APiece 
   * ... this.drawOnto(WorldScene) ...        - WorldScene 
   * ... this.isOnScreen(int, int) ...       -- boolean
   * ... this.collides(APiece) ...            - boolean
   * ... this.collidesEnemy(Enemy) ...        - boolean 
   * ... this.collidesShip(Spaceship) ...     - boolean
   * ... this.collidesEBullet(EBullet) ...    - boolean 
   * ... this.collidesSBullet(SBullet) ...    - boolean
   * METHODS FOR FIELDS 
   * ... this.location.draw(APiece, WorldScene) ...    - WorldScene
   */

  // draws the image of the APiece
  abstract WorldImage draw();

  // moves the location of the APiece
  abstract APiece move();

  // draws the image of the APiece onto the given WorldScene using its cartesian
  // point location
  public WorldScene drawOnto(WorldScene w) {
    return this.location.draw(this, w);
  }

  //checks to see if this is on the screen
  abstract boolean isOnScreen(int maxX, int maxY);

  // checks to see if this is colliding with other
  abstract boolean collides(APiece other);

  // checks to see if this is colliding with Enemy other
  public boolean collidesEnemy(Enemy other) {
    return false;
  }

  // checks to see if this is colliding with Spaceship other
  public boolean collidesShip(Spaceship other) {
    return false;
  }

  // checks to see if this is colliding with SBullet other
  public boolean collidesSBullet(SBullet other) {
    return false;
  }

  // checks to see if this is colliding with EBullet other
  public boolean collidesEBullet(EBullet other) {
    return false;
  }

}

// represents an Enemy which is a type of APiece
class Enemy extends APiece {
  int height;

  // the constructors
  Enemy(CartPt location, int height, int dx, int dy, boolean isOffScreen) {
    super(location, dx, dy, isOffScreen);
    this.height = height;

  }

  Enemy(int x, int y) {
    super(new CartPt(x, y), 0, 0, false);
    this.height = 20;
  }

  Enemy(int x, int y, int dx, int dy) {
    super(new CartPt(x, y), dx, dy, false);
    this.height = 20;
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.location ...          -- CartPt
   * ... this.height ...            -- int
   * ... this.dx ...                 - int
   * ... this.dy ...                 - int 
   * ... this.isOnScreen ...         - boolean
   * METHODS 
   * ... this.draw() ...                     -- WorldImage 
   * ... this.move() ...                     -- APiece 
   * ... this.isOnScreen(int, int) ...        - boolean 
   * ... this.shoot() ...                     - Enemy 
   * ... this.collides(APiece) ...            - boolean
   * ... this.collidesEnemy(Enemy) ...        - boolean 
   * ... this.collidesShip(Spaceship) ...     - boolean
   * ... this.collidesEBullet(EBullet) ...    - boolean 
   * ... this.collidesSBullet(SBullet) ...    - boolean
   * METHODS FOR FIELDS
   * ... this.location.draw(APiece, WorldScene) ...                        -- WorldScene 
   * ... this.location.moveCartPt(int, int) ...                            -- CartPt
   * ... this.location.checkWithinCartBounds(CartPt, int, int, int) ...    -- boolean
   */

  // draws the image of the Enemy
  public WorldImage draw() {
    return new RectangleImage(this.height, this.height, "solid", Color.magenta);
  }

  // moves this Enemy depending on its speeds along the x-axis and y-axis
  public Enemy move() {
    return new Enemy(this.location.moveCartPt(this.dx, this.dy), this.height, this.dx, this.dy,
                       this.isOffScreen);
  }

  // creates a new EBullet that shoots from the bottom middle of the enemy
  public EBullet shoot() {
    return new EBullet(this.location.moveCartPt(0, this.height / 2));
  }

  // checks to see if this is on the screen
  public boolean isOnScreen(int maxX, int maxY) {
    return true;
  }

  // checks to see if this Enemy is colliding with other
  public boolean collides(APiece other) {
    return other.collidesEnemy(this);
  }

  // checks to see if this Enemy is colliding with SBullet other
  public boolean collidesSBullet(SBullet other) {
    return this.location.checkWithinCartBounds(other.location, this.height, this.height,
        other.radius);
  }

}

// represents an enemy bullet (EBullet) which is a type of APiece
class EBullet extends APiece {
  int radius;

  // the constructors
  EBullet(CartPt location, int radius, int dx, int dy, boolean isOffScreen) {
    super(location, dx, dy, isOffScreen);
    this.radius = radius;
  }

  EBullet(CartPt location) {
    super(location, 0, 5, false);
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.location ...          -- CartPt
   * ... this.radius ...            -- int
   * ... this.dx ...                 - int
   * ... this.dy ...                 - int 
   * ... this.isOnScreen ...         - boolean
   * METHODS 
   * ... this.draw() ...                     -- WorldImage 
   * ... this.move() ...                     -- APiece 
   * ... this.isOnScreen(int, int) ...        - boolean 
   * ... this.shoot() ...                     - Enemy 
   * ... this.collides(APiece) ...            - boolean
   * ... this.collidesEnemy(Enemy) ...        - boolean 
   * ... this.collidesShip(Spaceship) ...     - boolean
   * ... this.collidesEBullet(EBullet) ...    - boolean 
   * ... this.collidesSBullet(SBullet) ...    - boolean
   * METHODS FOR FIELDS
   * ... this.location.draw(APiece, WorldScene) ...                        -- WorldScene 
   * ... this.location.moveCartPt(int, int) ...                            -- CartPt
   * ... this.location.checkWithinCartBounds(CartPt, int, int, int) ...    -- boolean
   */

  // draws the image of the EBullet
  public WorldImage draw() {
    return new CircleImage(5, "solid", Color.red);
  }

  // moves this EBullet depending on its speeds along the x-axis and y-axis
  public APiece move() {
    return new EBullet(this.location.moveCartPt(this.dx, this.dy), this.radius, this.dx, this.dy,
                         this.isOffScreen);
  }

  // checks to see if this EBullet is within the given bounds
  public boolean isOnScreen(int maxX, int maxY) {
    return this.location.checkWithinBounds(this.radius / 2, this.radius / 2, maxX - this.radius / 2,
        maxY - this.radius / 2);
  }

  // checks to see if this EBullet is colliding with other
  public boolean collides(APiece other) {
    return other.collidesEBullet(this);
  }

  // checks to see if this EBullet is colliding with Spaceship other
  public boolean collidesShip(Spaceship other) {
    return other.location.checkWithinCartBounds(this.location, other.width, other.height,
        this.radius);
  }

}

// Represents a spaceship which is an APiece
class Spaceship extends APiece {
  int width;
  int height;

  // the constructors
  Spaceship(CartPt location, int width, int height, int dx, int dy, boolean isOffScreen) {
    super(location, dx, dy, isOffScreen);
    this.width = width;
    this.height = height;
  }

  Spaceship(CartPt location, int dx, int dy) {
    super(location, dx, dy, false);
    this.width = 60;
    this.height = 30;
  }

  Spaceship() {
    super(new CartPt(300, 390), 0, 0, false);
    this.width = 60;
    this.height = 30;
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.location ...          -- CartPt
   * ... this.height ...            -- int
   * ... this.dx ...                 - int
   * ... this.dy ...                 - int 
   * ... this.isOnScreen ...         - boolean
   * METHODS 
   * ... this.draw() ...                     -- WorldImage 
   * ... this.move() ...                     -- APiece 
   * ... this.isOnScreen(int, int) ...        - boolean 
   * ... this.shoot() ...                     - Enemy 
   * ... this.collides(APiece) ...            - boolean
   * ... this.collidesEnemy(Enemy) ...        - boolean 
   * ... this.collidesShip(Spaceship) ...     - boolean
   * ... this.collidesEBullet(EBullet) ...    - boolean 
   * ... this.collidesSBullet(SBullet) ...    - boolean
   * METHODS FOR FIELDS
   * ... this.location.draw(APiece, WorldScene) ...                        -- WorldScene 
   * ... this.location.moveCartPt(int, int) ...                            -- CartPt
   * ... this.location.checkWithinCartBounds(CartPt, int, int, int) ...    -- boolean
   */

  // draws the spaceship
  public WorldImage draw() {
    return new RectangleImage(this.width, this.height, "solid", Color.black);
  }

  // moves the spaceship depending on its speeds along the x-axis and y-axis
  public Spaceship move() {
    return new Spaceship(this.location.moveCartPt(this.dx, this.dy), this.dx, this.dy);
  }

  // ensures that the location of this stays within the given bounds
  public Spaceship update(int maxX, int maxY) {
    return new Spaceship(this.location.setWithinBounds(this.width / 2, this.height / 2,
        maxX - this.width / 2, maxY - this.height / 2), this.dx, this.dy);
  }

  // sets the speed along the x-axis to the given int
  public Spaceship setDx(int dx) {
    return new Spaceship(this.location, dx, this.dy);
  }

  // produces a new SBullet that shoots from the top middle of this spaceship
  public SBullet shoot() {
    return new SBullet(this.location.moveCartPt(0, -this.height / 2));
  }

  //checks to see if this Spaceship is within the given bounds
  public boolean isOnScreen(int maxX, int maxY) {
    return true;
  }

  // checks to see if this Spaceship is colliding with other
  public boolean collides(APiece other) {
    return other.collidesShip(this);
  }

  // checks to see if this Spaceship is colliding with EBullet other
  public boolean collidesEBullet(EBullet other) {
    return other.collidesShip(this);
  }

}

// represents the Bullets being shot by the spaceship 
class SBullet extends APiece {
  int radius;

  // the constructors
  SBullet(CartPt location, int radius, int dx, int dy, boolean isOffScreen) {
    super(location, dx, dy, isOffScreen);
    this.radius = radius;

  }

  SBullet(CartPt location, int radius) {
    super(location, 0, -5, false);
    this.radius = radius;
  }

  SBullet(CartPt location) {
    super(location, 0, -5, false);
  }

  /*
   * TEMPLATE 
   * FIELDS 
   * ... this.location ...          -- CartPt
   * ... this.height ...            -- int
   * ... this.dx ...                 - int
   * ... this.dy ...                 - int 
   * ... this.isOnScreen ...         - boolean
   * METHODS 
   * ... this.draw() ...                     -- WorldImage 
   * ... this.move() ...                     -- APiece 
   * ... this.isOnScreen(int, int) ...        - boolean 
   * ... this.shoot() ...                     - Enemy 
   * ... this.collides(APiece) ...            - boolean
   * ... this.collidesEnemy(Enemy) ...        - boolean 
   * ... this.collidesShip(Spaceship) ...     - boolean
   * ... this.collidesEBullet(EBullet) ...    - boolean 
   * ... this.collidesSBullet(SBullet) ...    - boolean
   * METHODS FOR FIELDS
   * ... this.location.draw(APiece, WorldScene) ...                        -- WorldScene 
   * ... this.location.moveCartPt(int, int) ...                            -- CartPt
   * ... this.location.checkWithinCartBounds(CartPt, int, int, int) ...    -- boolean
   */

  // draws the Sbullet
  public WorldImage draw() {
    return new CircleImage(5, "solid", Color.green);
  }

  //moves the SBullet depending on its speeds along the x-axis and y-axis
  public APiece move() {
    return new SBullet(this.location.moveCartPt(this.dx, this.dy), this.radius);
  }

  //checks to see if this SBullet is within the given bounds
  public boolean isOnScreen(int maxX, int maxY) {
    return this.location.checkWithinBounds(this.radius / 2, this.radius / 2, maxX - this.radius / 2,
        maxY - radius);
  }

  // checks to see if this SBullet is colliding with other
  public boolean collides(APiece other) {
    return other.collidesSBullet(this);
  }

  // checks to see if this SBullet is colliding with Enemy other
  public boolean collidesEnemy(Enemy other) {
    return other.location.checkWithinCartBounds(this.location, other.height, other.height,
        this.radius);
  }

}

//represents the worldstate 
class SpaceInvaders extends World {
  IList<APiece> enemyList;
  IList<APiece> eBulletsList;
  IList<APiece> sBulletsList;
  Spaceship spaceship;
  Random rand;
  int tickCounter;

  // the constructor 
  SpaceInvaders(IList<APiece> enemyList, IList<APiece> eBulletsList, IList<APiece> sBulletsList,
      Spaceship spaceship, int tickCounter) {
    this.enemyList = enemyList;
    this.eBulletsList = eBulletsList;
    this.sBulletsList = sBulletsList;
    this.spaceship = spaceship;
    this.rand = new Random();
    this.tickCounter = tickCounter;

  }

  // constructor to test randomness
  SpaceInvaders(IList<APiece> enemyList, IList<APiece> eBulletsList, IList<APiece> sBulletsList,
      Spaceship spaceship, Random rand, int tickCounter) {
    this.enemyList = enemyList;
    this.eBulletsList = eBulletsList;
    this.sBulletsList = sBulletsList;
    this.spaceship = spaceship;
    this.rand = rand;
    this.tickCounter = tickCounter;
  }

  SpaceInvaders() {
    this.enemyList = new Utils().buildList(36, new TurnIntoEnemyRows(36));
    this.eBulletsList = new MtList<APiece>();
    this.sBulletsList = new MtList<APiece>();
    this.spaceship = new Spaceship();
    this.rand = new Random();
  }

  //constructor to test randomness
  SpaceInvaders(Random rand) {
    this.enemyList = new Utils().buildList(36, new TurnIntoEnemyRows(36));
    this.eBulletsList = new MtList<APiece>();
    this.sBulletsList = new MtList<APiece>();
    this.spaceship = new Spaceship();
    this.rand = rand;
  }

  /*
   * TEMPLATE
   * FIELDS 
   * ... this.enemyList ...  -- IList <APiece> 
   * ... this.eBulletsList ... -- IList<APiece> 
   * ... this.sBulletsList ... -- IList <APiece> 
   * ... this.spaceship ...   -- APiece
   * METHODS 
   * ... this.makeSceneHelper() ... -- WorldScene 
   * ... this.makeScene() ... -- WorldScene
   * ... this.onTick() ... -- World 
   * ... this.onKeyEvent() ... -- WorldScene 
   * ... this.spawnEBullets() ... -- WorldScene
   * ... this.worldEnds() ... -- WorldScene 
   * ... this.makeEndScene() ... -- WorldScene
   * METHODS FOR FIELDS 
   * ... this.spaceship.drawOnto(WorldScene)  ...             -WorldScene
   * ... this.spaceship.drawOnto(WorldScene) ...                 -WorldScene
   * ... this.sBulletsList.fold(BiFunction, WorldScene) ...         -WorldScene
   * ... this.eBulletsList.fold(BiFunction, WorldScene) ...      -WorldScene
   * ... this.enemyList.fold(BiFunction, WorldScene) ...      -WorldScene
   * ... this.enemyList.filter(Predicate) ...        -IList<APiece>
   * ... this.spaceship.move().update(WorldScene) ...     - WorldScene
   * ... this.eBulletsList.map(Function).filter(Predicate) ...         -IList<APiece>
   * ... this.sBulletsList.filter(Predicate).filter(Predicate).map(Function) ... -IList<APiece> 
   * ... this.spaceship.shoot() ... - APiece 
   * ... this.enemyList.getNth(int) ... - APiece
   * ... this.rand.nextInt(int) ... - int
   * ... this.enemyList.sizeOfList().shoot() ... - APiece 
   * ... this.enemyList.sizeOfList() ... - int
   */

  // draws the sbullets, ebullets, and enemies onto the worldscene
  public WorldScene makeSceneHelper() {
    return this.sBulletsList.fold(new DrawPiece(), this.eBulletsList.fold(new DrawPiece(),
        (this.enemyList.fold(new DrawPiece(), new WorldScene(600, 500)))));
  }

  // draws the spaceship onto the worldscene
  public WorldScene makeScene() {
    return this.spaceship.drawOnto(makeSceneHelper());
  }

  // changes the state of the world per tick
  public World onTick() {
    IList<APiece> currEnemyList = this.enemyList.filter(new IsNotCollidesList(this.sBulletsList));
    Spaceship currSpaceship = this.spaceship.move().update(600, 400);
    IList<APiece> currEBullets = this.eBulletsList.map(new MovePieces())
        .filter(new IsOnScreen(600, 400));
    IList<APiece> currSBullets = this.sBulletsList.filter(new IsOnScreen(600, 400))
        .filter(new IsNotCollidesList(this.enemyList)).map(new MovePieces());

    if (tickCounter % 56 == 0) {
      return new SpaceInvaders(currEnemyList, currEBullets, currSBullets, currSpaceship, rand,
          tickCounter + 1).spawnEBullets();
    }
    else {
      return new SpaceInvaders(currEnemyList, currEBullets, currSBullets, currSpaceship, rand,
          tickCounter + 1);
    }
  }

  // moves the spaceship and shoots the bullets according to the key pressed
  public World onKeyEvent(String key) {

    if (key.equals("right")) {
      return new SpaceInvaders(this.enemyList, this.eBulletsList, this.sBulletsList,
          this.spaceship.setDx(5), rand, tickCounter);
    }
    else if (key.equals("left")) {
      return new SpaceInvaders(this.enemyList, this.eBulletsList, this.sBulletsList,
          this.spaceship.setDx(-5), rand, tickCounter);
    }
    else if (key.equals(" ") && (this.sBulletsList.sizeOfList() < 3)) {
      SBullet sBulletSpawn = this.spaceship.shoot();
      return new SpaceInvaders(this.enemyList, this.eBulletsList,
          new ConsList<APiece>(sBulletSpawn, this.sBulletsList), this.spaceship, rand, tickCounter);
    }
    else {
      return this;
    }
  }

  // randomly spawns the Ebullets onto the screen if there aren't already 10 on the screen
  public World spawnEBullets() {
    if (this.eBulletsList.sizeOfList() < 10) {
      EBullet eBulletSpawn = ((Enemy) enemyList.getNth(this.rand.nextInt(enemyList.sizeOfList())))
          .shoot();
      // if we were to try to avoid casting in this situation we would have to make several copies
      // of the same function object specific to each class, resulting in code duplication
      return new SpaceInvaders(this.enemyList,
          new ConsList<APiece>(eBulletSpawn, this.eBulletsList), this.sBulletsList, this.spaceship,
          rand, tickCounter);
    }
    else {
      return this;
    }
  }

  // Checks to see if we need to end the game
  public WorldEnd worldEnds() {
    if (!(new IsNotCollidesList(this.eBulletsList).test(spaceship))
        || this.enemyList.sizeOfList() == 0) {
      return new WorldEnd(true, makeEndScene());
    }
    else {
      return new WorldEnd(false, this.makeEndScene());
    }
  }

  // makes the end scene for the game
  public WorldScene makeEndScene() {
    WorldScene endScene = new WorldScene(800, 600);
    return endScene.placeImageXY(new TextImage("Game Over!", 70, Color.pink), 300, 250);

  }

}

//examples and tests for the classes that represent Space Invaders
class ExamplesSpaceInvaders {
  ExamplesSpaceInvaders() {}

  // runs big bang
  boolean testBigBang(Tester t) {
    SpaceInvaders world = new SpaceInvaders();
    int worldWidth = 600;
    int worldHeight = 500;
    double tickRate = 1.0 / 28;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }

  // examples of spaceships
  Spaceship s1 = new Spaceship();
  Spaceship s2 = new Spaceship(new CartPt(250, 390), 60, 30, 50, 0, false);
  Spaceship s3 = new Spaceship(new CartPt(250, 390), 50, 20);
  Spaceship s4 = new Spaceship(new CartPt(300, 250), 50, 20);
  
  // examples of SBullets
  APiece sb1 = new SBullet(new CartPt(300, 250), 5);
  APiece sb2 = new SBullet(new CartPt(290, 250), 5);
  APiece sb3 = new SBullet(new CartPt(280, 250), 5);
  APiece sb4 = new SBullet(new CartPt(270, 250), 5);
  APiece sb5 = new SBullet(new CartPt(260, 250), 5);
  APiece sb6 = new SBullet(new CartPt(250, 250), 5);
  APiece sb7 = new SBullet(new CartPt(300, 250), 5);
  
  SBullet sbullet1 = new SBullet(new CartPt(300, 250), 5);
  
  // empty list of APiece
  IList<APiece> mt = new MtList<APiece>();

  // list of SBullets
  IList<APiece> lsb1 = new ConsList<APiece>(this.sb2,
      new ConsList<APiece>(this.sb3, new ConsList<APiece>(this.sb4,
          new ConsList<APiece>(this.sb5, new ConsList<APiece>(this.sb6, this.mt)))));
  IList<APiece> lsb2 = new ConsList<APiece>(this.sb2, this.mt);
  IList<APiece> lsb3 = new ConsList<APiece>(this.sb1, new ConsList<APiece>(this.sb2, this.mt));

  // examples of EBullets
  APiece eb1 = new EBullet(new CartPt(200, 300), 5, 0, 5, true);
  APiece eb2 = new EBullet(new CartPt(200, 310), 5, 0, 10, false);
  APiece eb3 = new EBullet(new CartPt(200, 320), 5, 10, 10, false);
  APiece eb4 = new EBullet(new CartPt(200, 330), 5, 3, 0, false);
  APiece eb5 = new EBullet(new CartPt(200, 340), 5, 0, 0, false);
  APiece eb6 = new EBullet(new CartPt(200, 350), 5, 0, 5, false);
  APiece eb7 = new EBullet(new CartPt(300, 250), 5, 0, 0, false);
  
  EBullet ebullet1 = new EBullet(new CartPt(300, 250), 5, 0, 0, false);
  EBullet ebullet2 = new EBullet(new CartPt(250, 390), 5, 0, 0, false);
  
  // list of EBullets
  IList<APiece> leb1 = new ConsList<APiece>(this.eb2,
      new ConsList<APiece>(this.eb3, new ConsList<APiece>(this.eb4,
          new ConsList<APiece>(this.eb5, new ConsList<APiece>(this.eb6, this.mt)))));
  IList<APiece> leb2 = new ConsList<APiece>(this.eb2, this.mt);
  IList<APiece> leb3 = new ConsList<APiece>(this.eb1, new ConsList<APiece>(this.eb2, this.mt));
  IList<APiece> leb4 = new ConsList<APiece>(this.eb2, new ConsList<APiece>(this.eb3, this.mt));

  // examples of Enemy
  APiece e1 = new Enemy(new CartPt(10, 10), 20, 0, 0, false);
  APiece e2 = new Enemy(new CartPt(10, 20), 20, 0, 0, false);
  APiece e3 = new Enemy(new CartPt(10, 30), 20, 0, 5, false);
  APiece e4 = new Enemy(new CartPt(10, 40), 20, 5, 0, false);
  APiece e5 = new Enemy(new CartPt(10, 50), 20, 5, 5, false);
  APiece e6 = new Enemy(10, 10, 5, 0);
  APiece e7 = new Enemy(5, 10, 0, 5);
  APiece e8 = new Enemy(10, 10);
  APiece e9 = new Enemy(new CartPt(300, 250), 20, 5, 5, false);
  
  Enemy enemy1 = new Enemy(new CartPt(300, 250), 20, 5, 5, false);
  Enemy enemy2 = new Enemy(new CartPt(30, 25), 20, 5, 5, false);
  Enemy enemy3 = new Enemy(new CartPt(300, 300), 20, 5, 5, false);

  // examples of enemy for buildList test
  APiece buildE1 = new Enemy(30, 30);
  APiece buildE2 = new Enemy(90, 30);

  // list of enemy for buildList test
  IList<APiece> buildEList1 = new ConsList<APiece>(this.buildE1, this.mt);
  IList<APiece> buildEList2 = new ConsList<APiece>(this.buildE1,
      new ConsList<APiece>(this.buildE2, this.mt));

  // list of enemy
  IList<APiece> elist1 = new ConsList<APiece>(this.e1, this.mt);
  IList<APiece> elist2 = new ConsList<APiece>(this.e1,
      new ConsList<APiece>(this.e2,
        new ConsList<APiece>(this.e3,
          new ConsList<APiece>(this.e4, this.mt))));

  // example of WorldScene
  WorldScene ws1 = new WorldScene(600, 500);

  // Examples of SpaceInvader World
  SpaceInvaders w1 = new SpaceInvaders(this.elist1, this.leb2, this.lsb2,
      this.s1, new Random(10), 1);
  SpaceInvaders w2 = new SpaceInvaders(this.elist1, this.leb4, this.lsb2,
      this.s1, new Random(10), 1);
  SpaceInvaders w3 = new SpaceInvaders(this.elist1, this.leb2, this.lsb2,
      this.s2, new Random(10), 1);
  SpaceInvaders w4 = new SpaceInvaders(this.elist1, this.leb4, this.lsb2,
      this.s2, new Random(10), 1);
  SpaceInvaders w8 = new SpaceInvaders(this.elist1, this.leb2, this.lsb2,
      this.s1, new Random(10), 56);

  // List of strings and integers for andmap/ormap tests
  IList<String> stringList1 = new ConsList<String>("apple",
      new ConsList<String>("abe", new MtList<String>()));
  IList<String> stringList2 = new ConsList<String>("apple",
      new ConsList<String>("bear", new MtList<String>()));
  IList<String> stringList3 = new ConsList<String>("khushi", new MtList<String>());
  IList<Integer> intList1 = new ConsList<Integer>(2,
      new ConsList<Integer>(4, new ConsList<Integer>(6, new MtList<Integer>())));
  IList<Integer> intList2 = new ConsList<Integer>(2,
      new ConsList<Integer>(4, new ConsList<Integer>(7, new MtList<Integer>())));
  IList<Integer> intList3 = new ConsList<Integer>(1,
      new ConsList<Integer>(3, new MtList<Integer>()));

  // examples of CartPt
  CartPt cp1 = new CartPt(1, 3);
  CartPt cp2 = new CartPt(0, 3);
  CartPt cp3 = new CartPt(1, 5);
  CartPt cp4 = new CartPt(1, 3);
  CartPt cp5 = new CartPt(5, 7);

  // list of CartPt
  IList<CartPt> mtPoints = new MtList<CartPt>();
  IList<CartPt> points1 = new ConsList<CartPt>(this.cp1, this.mtPoints);
  IList<CartPt> points2 = new ConsList<CartPt>(this.cp2,
      new ConsList<CartPt>(this.cp3, new ConsList<CartPt>(this.cp5, this.mtPoints)));
  IList<CartPt> points3 = new ConsList<CartPt>(this.cp4, this.points2);

  // example of WorldScenes
  WorldScene base = new WorldScene(600, 400);

  WorldScene expectedEnemyDraw = this.base.placeImageXY(this.e1.draw(), 1, 3);
  WorldScene expectedSpaceshipDraw = this.base.placeImageXY(this.s1.draw(), 0, 3);
  WorldScene expectedSBulletDraw = this.base.placeImageXY(this.sb1.draw(), 1, 5);
  WorldScene expectedEBulletDraw = this.base.placeImageXY(this.eb1.draw(), 1, 3);

  WorldScene expectedEnemyDrawPiece = this.base.placeImageXY(this.e1.draw(), 10, 10);
  WorldScene expectedSpaceshipDrawPiece = this.expectedEnemyDrawPiece.placeImageXY(this.s1.draw(),
      300, 390);
  WorldScene expectedSBulletDrawPiece = this.expectedSpaceshipDrawPiece
      .placeImageXY(this.sb1.draw(), 300, 250);
  WorldScene expectedEBulletDrawPiece = this.expectedSBulletDrawPiece.placeImageXY(this.eb1.draw(),
      200, 300);

  // examples of enemy
  APiece expectedEnemy1 = new Enemy(90, 210);
  APiece expectedEnemy2 = new Enemy(270, 210);
  APiece expectedEnemy3 = new Enemy(30, 210);

  // tests for draw() in Spaceship class
  boolean testDrawSpaceship(Tester t) {
    return t.checkExpect(this.s1.draw(), new RectangleImage(60, 30, "solid", Color.black))
        && t.checkExpect(this.s2.draw(), new RectangleImage(60, 30, "solid", Color.black))
        && t.checkExpect(this.s3.draw(), new RectangleImage(60, 30, "solid", Color.black));
  }

  // tests for draw() in EBullet class
  boolean testDrawEBullet(Tester t) {
    return t.checkExpect(this.eb1.draw(), new CircleImage(5, "solid", Color.red))
        && t.checkExpect(this.eb2.draw(), new CircleImage(5, "solid", Color.red));
  }

  // tests for draw() in SBullet class
  boolean testDrawSBullet(Tester t) {
    return t.checkExpect(this.sb1.draw(), new CircleImage(5, "solid", Color.green))
        && t.checkExpect(this.sb2.draw(), new CircleImage(5, "solid", Color.green));
  }

  // tests for draw() in Enemy class
  boolean testDrawEnemy(Tester t) {
    return t.checkExpect(this.e1.draw(), new RectangleImage(20, 20, "solid", Color.magenta))
        && t.checkExpect(this.e2.draw(), new RectangleImage(20, 20, "solid", Color.magenta))
        && t.checkExpect(this.e6.draw(), new RectangleImage(20, 20, "solid", Color.magenta))
        && t.checkExpect(this.e7.draw(), new RectangleImage(20, 20, "solid", Color.magenta))
        && t.checkExpect(this.e8.draw(), new RectangleImage(20, 20, "solid", Color.magenta));
  }

  // tests for move() in Spaceship class
  boolean testMoveSpaceship(Tester t) {
    return t.checkExpect(this.s1.move(), new Spaceship(new CartPt(300, 390), 0, 0))
        && t.checkExpect(this.s2.move(), new Spaceship(new CartPt(300, 390), 50, 0))
        && t.checkExpect(this.s3.move(), new Spaceship(new CartPt(300, 410), 50, 20));
  }

  // tests for move() in EBullet class
  boolean testMoveEBullet(Tester t) {
    return t.checkExpect(this.eb1.move(), new EBullet(new CartPt(200, 305), 5, 0, 5, true))
        && t.checkExpect(this.eb2.move(), new EBullet(new CartPt(200, 320), 5, 0, 10, false))
        && t.checkExpect(this.eb3.move(), new EBullet(new CartPt(210, 330), 5, 10, 10, false));
  }

  // tests for move() in SBullet class
  boolean testMoveSBullet(Tester t) {
    return t.checkExpect(this.sb1.move(), new SBullet(new CartPt(300, 245), 5))
        && t.checkExpect(this.sb2.move(), new SBullet(new CartPt(290, 245), 5))
        && t.checkExpect(this.sb3.move(), new SBullet(new CartPt(280, 245), 5));
  }

  // test for move enemy
  boolean testMoveEnemy(Tester t) {
    return t.checkExpect(this.e1.move(), new Enemy(new CartPt(10, 10), 20, 0, 0, false))
        && t.checkExpect(this.e7.move(), new Enemy(new CartPt(5, 15), 20, 0, 5, false))
        && t.checkExpect(this.e8.move(), new Enemy(new CartPt(10, 10), 20, 0, 0, false));

  }

  // test for drawonto
  boolean testDrawOnto(Tester t) {
    return t.checkExpect(this.s2.drawOnto(this.ws1),
        this.ws1.placeImageXY(new RectangleImage(60, 30, "solid", Color.black), 250, 390))
        && t.checkExpect(this.sb2.drawOnto(this.ws1),
            this.ws1.placeImageXY(new CircleImage(5, "solid", Color.green), 290, 250))
        && t.checkExpect(this.eb2.drawOnto(this.ws1),
            this.ws1.placeImageXY(new CircleImage(5, "solid", Color.red), 200, 310))
        && t.checkExpect(this.e1.drawOnto(this.ws1),
            this.ws1.placeImageXY(new RectangleImage(20, 20, "solid", Color.magenta), 10, 10));
  }
 
  // test for andmap/ormap using strings
  Predicate<String> testAndOrMapString = new TestAndOrMapString();

  boolean testAndOrMapString(Tester t) {
    return t.checkExpect(this.stringList1.andmap(testAndOrMapString), true)
        && t.checkExpect(this.stringList2.andmap(testAndOrMapString), false)
        && t.checkExpect(new MtList<String>().andmap(testAndOrMapString), true)
        && t.checkExpect(this.stringList2.ormap(testAndOrMapString), true)
        && t.checkExpect(this.stringList3.ormap(testAndOrMapString), false)
        && t.checkExpect(new MtList<String>().ormap(testAndOrMapString), false);
  }

  // test for andmap/ormap using integers
  Predicate<Integer> testAndOrMapInt = new TestAndOrMapInt();

  boolean testAndOrMapInt(Tester t) {
    return t.checkExpect(this.intList1.andmap(testAndOrMapInt), true)
        && t.checkExpect(this.intList2.andmap(testAndOrMapInt), false)
        && t.checkExpect(new MtList<Integer>().andmap(testAndOrMapInt), true)
        && t.checkExpect(this.intList2.ormap(testAndOrMapInt), true)
        && t.checkExpect(this.intList3.ormap(testAndOrMapInt), false)
        && t.checkExpect(new MtList<Integer>().ormap(testAndOrMapInt), false);
  }

  // tests for buildlist
  boolean testBuildList(Tester t) {
    return t.checkExpect(new Utils().buildList(1, new TurnIntoEnemyRows(1)), this.buildEList1)
        && t.checkExpect(new Utils().buildList(2, new TurnIntoEnemyRows(2)), this.buildEList2)
        && t.checkExpect(new Utils().buildList(0, new TurnIntoEnemyRows(0)), this.mt);
  }

  // tests for isOnScreen with enemies and spaceships
  boolean testisOnScreen(Tester t) {
    // enemies and spaceships are always on screen
    return t.checkExpect(e1.isOnScreen(30, 20), true)
        && t.checkExpect(e2.isOnScreen(5, 5), true)
        && t.checkExpect(s1.isOnScreen(20, 20), true)
        && t.checkExpect(s2.isOnScreen(5, 5), true);

  }
  
  // tests for isOnScreen with eBullets and sBullets
  boolean testBulletIsOnScreen(Tester t) {
    return t.checkExpect(eb1.isOnScreen(5, 10), false)
        && t.checkExpect(eb2.isOnScreen(200, 310), false)
        && t.checkExpect(eb5.isOnScreen(300, 350), true)
        && t.checkExpect(sb1.isOnScreen(400, 300), true)
        && t.checkExpect(sb2.isOnScreen(290, 250), false)
        && t.checkExpect(sb3.isOnScreen(10, 5), false);
    
  }
  
  // tests for collides within enemy class
  boolean testEnemyCollides(Tester t) {
    return t.checkExpect(eb7.collides(e9), false)
        && t.checkExpect(sb1.collides(e9), true)
        && t.checkExpect(sb6.collides(e9), false)
        && t.checkExpect(s2.collides(e9), false);
  }
  
  // tests for collides within ebullet class
  boolean testEBulletCollides(Tester t) {
    return t.checkExpect(e9.collides(eb7), false)
        && t.checkExpect(s1.collides(eb3), false)
        && t.checkExpect(s4.collides(eb7), true)
        && t.checkExpect(sb1.collides(eb3), false);
  }
  
  //test for collides within sbullet class
  boolean testSBulletCollides(Tester t) {
    return t.checkExpect(e9.collides(sb6), false)
        && t.checkExpect(e9.collides(sb7), true)
        && t.checkExpect(s2.collides(sb7), false)
        && t.checkExpect(sb1.collides(sb7), false);
  }
  
  //test for collides within spaceship class
  boolean testSpaceshipCollides(Tester t) {
    return t.checkExpect(e9.collides(s1), false)
        && t.checkExpect(sb1.collides(s1), false)
        && t.checkExpect(eb1.collides(s1), false);
  }
  
  // tests for collidesEnemy
  boolean testCollidesEnemy(Tester t) {
    return t.checkExpect(sb1.collidesEnemy(enemy1), true)
        && t.checkExpect(sb2.collidesEnemy(enemy2), false)
        && t.checkExpect(eb1.collidesEnemy(enemy1), false)
        && t.checkExpect(s1.collidesEnemy(enemy1), false);

  }
  
  //tests for collidesEBullet
  boolean testCollidesEBullet(Tester t) {
    return t.checkExpect(sb3.collidesEBullet(ebullet1), false)
        && t.checkExpect(e7.collidesEBullet(ebullet1), false)
        && t.checkExpect(s1.collidesEBullet(ebullet1), false)
        && t.checkExpect(s3.collidesEBullet(ebullet2), true);
  }
  
  //tests for collidesSBullet
  boolean testCollidesSBullet(Tester t) {
    return t.checkExpect(eb1.collidesSBullet(sbullet1), false)
        && t.checkExpect(e2.collidesSBullet(sbullet1), false)
        && t.checkExpect(e9.collidesSBullet(sbullet1), true)
        && t.checkExpect(s2.collidesSBullet(sbullet1), false);
  }

  // tests for collidesShip
  boolean testCollidesShip(Tester t) {
    return t.checkExpect(e5.collidesShip(s1), false)
        && t.checkExpect(sb3.collidesShip(s1), false)
        && t.checkExpect(eb5.collidesShip(s1), false)
        && t.checkExpect(eb7.collidesShip(s4), true);
  }
  
  // tests for shoot in enemy class
  boolean testShootEnemy(Tester t) {
    return t.checkExpect(enemy1.shoot(), new EBullet(new CartPt(300, 260)))
        && t.checkExpect(enemy2.shoot(), new EBullet(new CartPt(30, 35)))
        && t.checkExpect(enemy3.shoot(), new EBullet(new CartPt(300, 310)));
  }
  
  // tests for shoot in spaceship class
  boolean testShootShip(Tester t) {
    return t.checkExpect(s2.shoot(), new SBullet(new CartPt(250, 375), 0, 0, -5, false))
        && t.checkExpect(s3.shoot(), new SBullet(new CartPt(250, 375), 0, 0, -5, false))
        && t.checkExpect(s1.shoot(), new SBullet(new CartPt(300, 375), 0, 0, -5, false));
  }
  
  // tests for setDx in spaceship class
  boolean testSetDx(Tester t) {
    return t.checkExpect(s2.setDx(5), new Spaceship(new CartPt(250, 390), 5, 0))
        && t.checkExpect(s1.setDx(0), new Spaceship(new CartPt(300, 390), 0, 0))
        && t.checkExpect(s3.setDx(10), new Spaceship(new CartPt(250, 390), 10, 20));
  }
  
  // tests for update in spaceship class
  boolean testUpdate(Tester t) {
    return t.checkExpect(s1.update(30, 10), new Spaceship(new CartPt(30,15), 60, 30, 0, 0, false))
        && t.checkExpect(s2.update(0, 0), new Spaceship(new CartPt(30, 15), 50, 0))
        && t.checkExpect(s3.update(5, 0), new Spaceship(new CartPt(30, 15), 50, 20));
  }
  
  // test for makeScene
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.w3.makeScene(),
        this.ws1.placeImageXY(new RectangleImage(20, 20, "solid", Color.magenta), 10, 10)
            .placeImageXY(new CircleImage(5, "solid", Color.red), 200, 310)
            .placeImageXY(new CircleImage(5, "solid", Color.green), 290, 250)
            .placeImageXY(new RectangleImage(60, 30, "solid", Color.black), 250, 390))

        && t.checkExpect(this.w4.makeScene(),
            this.ws1.placeImageXY(new RectangleImage(20, 20, "solid", Color.magenta), 10, 10)
                .placeImageXY(new CircleImage(5, "solid", Color.red), 200, 320)
                .placeImageXY(new CircleImage(5, "solid", Color.red), 200, 310)
                .placeImageXY(new CircleImage(5, "solid", Color.green), 290, 250)
                .placeImageXY(new RectangleImage(60, 30, "solid", Color.black), 250, 390));

  }

  // test for makeSceneHelper
  boolean testMakeSceneHelper(Tester t) {
    return t.checkExpect(this.w1.makeSceneHelper(),
        this.ws1.placeImageXY(new RectangleImage(20, 20, "solid", Color.magenta), 10, 10)
            .placeImageXY(new CircleImage(5, "solid", Color.red), 200, 310)
            .placeImageXY(new CircleImage(5, "solid", Color.green), 290, 250))
        && t.checkExpect(this.w2.makeSceneHelper(),
            this.ws1.placeImageXY(new RectangleImage(20, 20, "solid", Color.magenta), 10, 10)
                .placeImageXY(new CircleImage(5, "solid", Color.red), 200, 310)
                .placeImageXY(new CircleImage(5, "solid", Color.red), 200, 320)
                .placeImageXY(new CircleImage(5, "solid", Color.green), 290, 250));
  }

  // tests for moveCartPt(int, int) in CartPt class
  boolean testMoveCartPt(Tester t) {
    return t.checkExpect(this.cp1.moveCartPt(0, 0), this.cp1)
        && t.checkExpect(this.cp5.moveCartPt(34, 0), new CartPt(39, 7))
        && t.checkExpect(this.cp4.moveCartPt(0, 12), new CartPt(1, 15))
        && t.checkExpect(this.cp3.moveCartPt(5, 3), new CartPt(6, 8));
  }

  // tests for equalsCartPt(CartPt) in CartPt class
  boolean testEqualsCartPt(Tester t) {
    return t.checkExpect(this.cp1.equalsCartPt(this.cp2), false)
        && t.checkExpect(this.cp1.equalsCartPt(this.cp4), true);
  }

  // tests for drawCartPt(APiece, WorldScene) in CartPt class
  boolean testDrawCartPt(Tester t) {
    return t.checkExpect(this.cp1.draw(this.e1, base), this.expectedEnemyDraw)
        && t.checkExpect(this.cp2.draw(this.s1, base), this.expectedSpaceshipDraw)
        && t.checkExpect(this.cp3.draw(this.sb1, base), this.expectedSBulletDraw)
        && t.checkExpect(this.cp4.draw(this.eb1, base), this.expectedEBulletDraw);
  }

  // tests for test(CartPt) in EqualCartPt Predicate object
  boolean testTestEqualCartPt(Tester t) {
    return t.checkExpect(new EqualCartPt(this.cp1).test(this.cp2), false)
        && t.checkExpect(new EqualCartPt(this.cp1).test(this.cp4), true);
  }

  // tests for test(CartPt) in Contains Predicate object
  boolean testTestContains(Tester t) {
    return t.checkExpect(new Contains(this.mtPoints).test(this.cp5), false)
        && t.checkExpect(new Contains(this.points1).test(this.cp4), true)
        && t.checkExpect(new Contains(this.points2).test(this.cp1), false)
        && t.checkExpect(new Contains(this.points3).test(this.cp1), true);
  }

  // tests for apply(APiece, WorldScene) in DrawPiece Function object
  boolean testApplyDrawPiece(Tester t) {
    return t.checkExpect(new DrawPiece().apply(this.e1, this.base), this.expectedEnemyDrawPiece)
        && t.checkExpect(new DrawPiece().apply(this.s1, this.expectedEnemyDrawPiece),
            this.expectedSpaceshipDrawPiece)
        && t.checkExpect(new DrawPiece().apply(this.sb1, this.expectedSpaceshipDrawPiece),
            this.expectedSBulletDrawPiece)
        && t.checkExpect(new DrawPiece().apply(this.eb1, this.expectedSBulletDrawPiece),
            this.expectedEBulletDrawPiece);
  }

  // tests for apply(Integer) in TurnIntoEnemyRows Function object
  boolean testApplyTurnIntoEnemyRows(Tester t) {
    return t.checkExpect(new TurnIntoEnemyRows(36).apply(8), this.expectedEnemy1)
        && t.checkExpect(new TurnIntoEnemyRows(40).apply(9), this.expectedEnemy2)
        && t.checkExpect(new TurnIntoEnemyRows(30).apply(3), this.expectedEnemy3);
  }

  // tests for getNth(int) in IList
  boolean testGetNth(Tester t) {
    // tests for empty list and index too high
    return t.checkException(new IndexOutOfBoundsException("index is too high"), this.mtPoints,
        "getNth", 0)
        && t.checkException(new IndexOutOfBoundsException("index is too high"), this.leb1, "getNth",
            45)
        // tests for working cases
        && t.checkExpect(this.stringList1.getNth(0), "apple")
        && t.checkExpect(this.intList1.getNth(1), 4);
  }

  // tests for sizeOfList() in IList
  boolean testSizeOfList(Tester t) {
    return t.checkExpect(this.mt.sizeOfList(), 0) && t.checkExpect(this.leb2.sizeOfList(), 1)
        && t.checkExpect(this.lsb1.sizeOfList(), 5);
  }

  // tests for apply in MovePieces
  MovePieces movePieces = new MovePieces();

  boolean testApplyMovePieces(Tester t) {
    return t.checkExpect(this.movePieces.apply(this.e1),
        new Enemy(new CartPt(10, 10), 20, 0, 0, false))
        && t.checkExpect(this.movePieces.apply(this.eb2),
            new EBullet(new CartPt(200, 320), 5, 0, 10, false))
        && t.checkExpect(this.movePieces.apply(this.sb3), new SBullet(new CartPt(280, 245), 5))
        && t.checkExpect(this.movePieces.apply(this.s1), new Spaceship(new CartPt(300, 390), 0, 0));
  }

  // tests for test in IsNotCollidesList
  IsNotCollidesList isNotCollidesList1 = new IsNotCollidesList(this.leb1);
  IsNotCollidesList isNotCollidesList2 = new IsNotCollidesList(this.lsb1);
  IsNotCollidesList isNotCollidesList3 = new IsNotCollidesList(this.mt);
  APiece collidingS = new Spaceship(new CartPt(200, 300), 0, 0);
  APiece nonCollidingE = new Enemy(290, 500);

  boolean testTestIsNotCollidesList(Tester t) {
    // bullets cannot collide with each other
    return t.checkExpect(this.isNotCollidesList1.test(this.eb1), true)
        && t.checkExpect(this.isNotCollidesList2.test(this.sb2), true)
        // bullets may or may not collide with the opposing ship
        && t.checkExpect(this.isNotCollidesList1.test(this.collidingS), false)
        && t.checkExpect(this.isNotCollidesList2.test(this.nonCollidingE), true)
        && t.checkExpect(this.isNotCollidesList3.test(this.collidingS), true)
        && t.checkExpect(this.isNotCollidesList3.test(this.nonCollidingE), true);
  }

  // tests for test in IsNotCollides
  IsNotCollides isNotCollides1 = new IsNotCollides(this.eb1);
  IsNotCollides isNotCollides2 = new IsNotCollides(this.sb3);

  boolean testTestIsNotCollides(Tester t) {
    // bullets cannot collide with each other
    return t.checkExpect(this.isNotCollides1.test(this.eb1), true)
        && t.checkExpect(this.isNotCollides2.test(this.sb6), true)
        // bullets may or may not collide with the opposing ship
        && t.checkExpect(this.isNotCollides1.test(this.collidingS), false)
        && t.checkExpect(this.isNotCollides2.test(this.nonCollidingE), true);
  }

  // tests for test in IsOnScreen
  IsOnScreen tinyScreen = new IsOnScreen(100, 100);
  IsOnScreen normalScreen = new IsOnScreen(600, 400);

  boolean testTestIsOnScreen(Tester t) {
    return t.checkExpect(this.tinyScreen.test(this.e1), true)
        && t.checkExpect(this.tinyScreen.test(this.eb2), false)
        && t.checkExpect(this.normalScreen.test(this.s2), true)
        && t.checkExpect(this.normalScreen.test(new SBullet(new CartPt(1000, 1000))), false);
  }

  // tests for setWithinBounds
  boolean testSetWithinBounds(Tester t) {
    return t.checkExpect(this.cp1.setWithinBounds(2, 2, 5, 5), new CartPt(2, 3))
        && t.checkExpect(this.cp2.setWithinBounds(0, 0, 50, 50), this.cp2)
        && t.checkExpect(this.cp1.setWithinBounds(0, 0, 1, 3), this.cp1)
        && t.checkExpect(this.cp3.setWithinBounds(1, 5, 2, 6), this.cp3)
        && t.checkExpect(this.cp4.setWithinBounds(0, 0, 1, 2), new CartPt(1, 2))
        && t.checkExpect(this.cp5.setWithinBounds(6, 6, 12, 12), new CartPt(6, 7));
  }

  // tests for checkWithinBounds
  boolean testCheckWithinBounds(Tester t) {
    return t.checkExpect(this.cp1.checkWithinBounds(2, 2, 5, 5), false)
        && t.checkExpect(this.cp2.checkWithinBounds(0, 0, 50, 50), true)
        // edge cases
        && t.checkExpect(this.cp1.checkWithinBounds(0, 0, 1, 3), true)
        && t.checkExpect(this.cp3.checkWithinBounds(1, 5, 2, 6), true)
        && t.checkExpect(this.cp4.checkWithinBounds(0, 0, 1, 2), false)
        && t.checkExpect(this.cp5.checkWithinBounds(6, 6, 12, 12), false);
  }

  // tests for checkWithinCartBounds
  boolean testCheckWithinCartBounds(Tester t) {
    return t.checkExpect(this.cp1.checkWithinCartBounds(this.cp4, 5, 10, 2), true)
        && t.checkExpect(this.cp2.checkWithinCartBounds(this.cp5, 2, 3, 1), false);
  }

  // tests for onKeyEvent in SpaceInvadersWorld
  SpaceInvaders expectedKeyWorld1 = new SpaceInvaders(this.elist1, this.leb2, this.lsb2,
      this.s1.setDx(5), new Random(10), 1);
  SpaceInvaders expectedKeyWorld2 = new SpaceInvaders(this.elist1, this.leb4, this.lsb2,
      this.s1.setDx(-5), new Random(10), 1);
  SpaceInvaders expectedKeyWorld3 = new SpaceInvaders(this.elist1, this.leb2,
      new ConsList<APiece>(this.s1.shoot(), this.lsb2), this.s1, new Random(10), 1);

  IList<APiece> threeSBullets = new ConsList<APiece>(this.sb1, this.lsb3);
  SpaceInvaders w5 = new SpaceInvaders(this.elist1, this.leb1, this.threeSBullets, this.s2,
      new Random(10), 1);
  SpaceInvaders w6 = new SpaceInvaders(this.elist1, this.leb1, this.lsb1, this.s2, new Random(10),
      1);

  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(this.w1.onKeyEvent("right"), this.expectedKeyWorld1)
        && t.checkExpect(this.w2.onKeyEvent("left"), this.expectedKeyWorld2)
        // sBulletsList size is 1
        && t.checkExpect(this.w1.onKeyEvent(" "), this.expectedKeyWorld3)
        && t.checkExpect(this.w5.onKeyEvent(" "), this.w5)
        && t.checkExpect(this.w6.onKeyEvent(" "), this.w6)
        && t.checkExpect(this.w4.onKeyEvent("x"), this.w4);
  }

  // tests for makeEndScene
  WorldScene gameOver = new WorldScene(800, 600)
      .placeImageXY(new TextImage("Game Over!", 70, Color.pink), 300, 250);

  boolean testMakeEndScene(Tester t) {
    return t.checkExpect(this.w1.makeEndScene(), this.gameOver)
        && t.checkExpect(this.w2.makeEndScene(), this.gameOver)
        && t.checkExpect(this.w3.makeEndScene(), this.gameOver)
        && t.checkExpect(this.w4.makeEndScene(), this.gameOver);
  }

  // tests for worldEnds
  WorldEnd over = new WorldEnd(true, gameOver);
  WorldEnd notOver = new WorldEnd(false, gameOver);

  APiece collidingEBullet = new EBullet(new CartPt(300, 390), 5, 0, 10, false);
  IList<APiece> collidingEBulletList = new ConsList<APiece>(this.collidingEBullet, this.mt);
  SpaceInvaders shipCollided = new SpaceInvaders(this.elist1, this.collidingEBulletList, this.lsb2,
      this.s1, new Random(10), 1);

  SpaceInvaders noEnemies = new SpaceInvaders(this.mt, this.leb2, this.lsb2, this.s1,
      new Random(10), 1);

  boolean testWorldEnds(Tester t) {
    return t.checkExpect(this.w1.worldEnds(), this.notOver)
        && t.checkExpect(this.shipCollided.worldEnds(), this.over)
        && t.checkExpect(this.noEnemies.worldEnds(), this.over);
  }

  // tests for spawnEBullets
  IList<APiece> tenEBulletsList = new ConsList<APiece>(this.eb1,
      new ConsList<APiece>(this.eb2, new ConsList<APiece>(this.eb3,
          new ConsList<APiece>(this.eb4, new ConsList<APiece>(this.eb5, this.leb1)))));
  SpaceInvaders tenEBulletsWorld = new SpaceInvaders(this.elist1, this.tenEBulletsList, this.lsb2,
      this.s1, new Random(10), 1);

  SpaceInvaders expectedSpawn = new SpaceInvaders(this.elist1,
      new ConsList<APiece>(
          ((Enemy) this.elist1.getNth(new Random(10).nextInt(this.elist1.sizeOfList()))).shoot(),
          this.leb2),
      this.lsb2, this.s1, new Random(10), 1);

  boolean testSpawnEBullets(Tester t) {
    return t.checkExpect(this.tenEBulletsWorld.spawnEBullets(), this.tenEBulletsWorld)
        && t.checkExpect(this.w1.spawnEBullets(), this.expectedSpawn);
  }
  
  // tests for onTick
  IList<APiece> expectedEList1 = (new ConsList<APiece>(this.e1, this.mt));
  IList<APiece> expectedEBList2 = new ConsList<APiece>(
      new EBullet(new CartPt(10, 20), 0, 0, 5, false),
      new ConsList<APiece>(new EBullet(new CartPt(200, 320), 5, 0, 10, false), this.mt));
  IList<APiece> expectedSBList3 = new ConsList<APiece>(
      new SBullet(new CartPt(290, 245), 5, 0, -5, false), this.mt);
  Spaceship expectedS4 = new Spaceship(new CartPt(300, 385), 0, 0);

  IList<APiece> expectedEList5 = (new ConsList<APiece>(this.e1, this.mt));
  IList<APiece> expectedEBList6 = new ConsList<APiece>(
      new EBullet(new CartPt(200, 320), 5, 0, 10, false), this.mt);
  IList<APiece> expectedSBList7 = new ConsList<APiece>(
      new SBullet(new CartPt(290, 245), 5, 0, -5, false), this.mt);
  Spaceship expectedS8 = new Spaceship(new CartPt(300, 385), 0, 0);

  boolean testOnTick(Tester t) {
    return t.checkExpect(this.w8.onTick(),
        new SpaceInvaders(this.expectedEList1, expectedEBList2, expectedSBList3, expectedS4,
            new Random(), 57))
        && t.checkExpect(this.w1.onTick(), new SpaceInvaders(this.expectedEList5, expectedEBList6,
            expectedSBList7, expectedS8, new Random(), 2));
  }


}
