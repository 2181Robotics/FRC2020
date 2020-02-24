package recording;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.Command;

public class RecordedJoystick {

    private Joystick j;
    private SpecialButton jb;
    public boolean replay = false;
    private Timer clock = new Timer();
    private Saved last;
    public Saved start;
    public boolean recording = false;
    public String currFile = "";
    public int total = 0;
    private boolean[] joys;

    public Joystick getJoystick() {
        return j;
    }

    public void startReplay() {
        int b = j.getButtonCount();
        int jo = j.getAxisCount();
        boolean[] buttons = new boolean[b];
        for (int i = 0; i<b; i++) buttons[i] = true;
        boolean[] joys = new boolean[jo];
        for (int i = 0; i<jo; i++) joys[i] = true;
        startReplay(buttons, joys);
    }

    public void startReplay(boolean[] buttons, boolean[] joys) {
        if (jb != null) jb.configure(buttons);
        this.joys = joys;
        clock.reset();
        clock.start();
        replay = true;
    }

    public void stopReplay() {
        if (jb != null) jb.configure(new boolean[j.getButtonCount()]);
        joys = new boolean[j.getAxisCount()];
        replay = false;
        clock.stop();
    }

    public void startRecord() {
        if (!recording) {
            total = 1;
            recording = true;
            clock.reset();
            clock.start();
            last = new Saved(j, clock.get());
            start = last;
        }
    }

    public Saved makePlaceHolder() {
        return new Saved(j, 0);
    }

    public void stepRecord() {
        if (recording) {
            last.next = new Saved(j, clock.get());
            last = last.next;
            total++;
        }
    }

    public void stopRecord() {
        recording = false;
        clock.stop();
        //Save(currFile+part, true);
    }

    public boolean isDone() {
        return (start.next==null);
    }

    public double getTime() {
        return clock.get();
    }

    public RecordedJoystick(int port) {
        j = new Joystick(port);
        start = makePlaceHolder();
        head = new BinaryNode("", null);
        joys = new boolean[j.getAxisCount()];
    }

    public double getRawAxis(int axis) {
        try {
            if (!joys[axis]) {
                return j.getRawAxis(axis);
            } else {
                while (start.next!=null && start.next.time<clock.get()) {
                    start = start.next;
                }
                return start.joys[axis];
            }
        } catch (Exception e) {
            joys = new boolean[j.getAxisCount()];
            return 0;
        }
    }

    public void whenPressed(int button, Command command) {
        jb = new SpecialButton(j, button, jb, this);
        jb.whenPressed(command);
    }

    public void whenReleased(int button, Command command) {
        jb = new SpecialButton(j, button, jb, this);
        jb.whenReleased(command);
    }

    public void whileHeld(int button, Command command) {
        jb = new SpecialButton(j, button, jb, this);
        jb.whileHeld(command); //command is started repeatedly while button is held, possibly interrupting itself
    }

    public void whenHeld(int button, Command command) {
        jb = new SpecialButton(j, button, jb, this);
        jb.whenHeld(command);
    }

    public void toggleWhenPressed(int button, Command command) {
        jb = new SpecialButton(j, button, jb, this);
        jb.toggleWhenPressed(command);
    }

    public void cancelWhenPressed(int button, Command command) {
        jb = new SpecialButton(j, button, jb, this);
        jb.cancelWhenPressed(command);
    }

    private class SpecialButton extends Button {
        private Joystick j;
        private int button;
        private SpecialButton jb;
        private RecordedJoystick rj;
        private boolean replay = false;
        
        public SpecialButton(Joystick j, int button, SpecialButton jb, RecordedJoystick rj) {
            this.j = j;
            this.button = button;
            this.jb = jb;
            this.rj = rj;
        }

        public boolean get() {
            // returns true when button is supposed to be active
            if (!replay) {
                return j.getRawButton(button);
            } else {
                while (rj.start.next!=null && rj.start.next.time<rj.clock.get()) {
                    rj.start = rj.start.next;
                }
                return (rj.start.butts&(1 << button-1)) != 0;
            }
        }

        public void configure(boolean[] buttons) {
            replay = buttons[button-1];
            if (jb != null) jb.configure(buttons);
        }
    }

    private class BinaryNode {
        private String key;
        private Saved value;
        private BinaryNode left;
        private BinaryNode right;

        public BinaryNode(String key, Saved value) {
            this.key = key;
            this.value = value;
        }

        public void add(BinaryNode node) {
            int val = key.compareTo(node.key);
            if (val>0) {
                if (right==null) {
                    right = node;
                } else {
                    right.add(node);
                }
            } else {
                if (left==null) {
                    left = node;
                } else {
                    left.add(node);
                }
            }
        }

        public BinaryNode find(String name) {
            if (key.equals(name)) {
                return this;
            } else {
                int val = key.compareTo(name);
                if (val>0) {
                    if (right!=null) {
                        return right.find(name);
                    } else {
                        return null;
                    }
                } else {
                    if (left!=null) {
                        return left.find(name);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private BinaryNode head;

    public void add(String name, Saved value) {
        BinaryNode node = new BinaryNode(name, value);
        head.add(node);
    }

    public void updateReplay(String name, Saved start) {
        BinaryNode toUpdate = head.find(name);
        if (toUpdate!=null) {
            toUpdate.value.next = start;
        }
    }

    public boolean exists(String name) {
        return (head.find(name)!=null);
    }

    public void set(String name) {
        BinaryNode thing = head.find(name);
        if (thing!=null) {
            start = thing.value.next;
        } else {
            System.out.println("Error finding recording");
        }
    }
}