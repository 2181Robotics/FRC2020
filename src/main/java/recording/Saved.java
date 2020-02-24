package recording;

import java.io.Serializable;

import edu.wpi.first.wpilibj.Joystick;

public class Saved implements Serializable {
    private static final long serialVersionUID = 2181;

    public transient Saved next;
    public double time;
    public double[] joys;
    public int butts;

    public Saved(Joystick j, double startTime) {
        time = startTime;
        joys = new double[j.getAxisCount()];
        for (int i=0; i<j.getAxisCount(); i++) {
            joys[i] = j.getRawAxis(i);
        }
        butts = 0;
        for (int i=1; i<=j.getButtonCount(); i++) {
            if (j.getRawButton(i)) butts |= (1 << (i-1));
        }
    }

    // private void writeObject(ObjectOutputStream oos) 
    // throws IOException {
    //     oos.defaultWriteObject();
    //     oos.writeObject(time);
    //     oos.writeObject(joys);

    //     // oos.writeObject(butts);
    //     int l = butts.length;
    //     int t = 0;
    //     for (int i=0;i<l;++i) {
    //         if (butts[i]) t |= (1 << i);
    //     }
    //     oos.writeObject(l);
    //     oos.writeObject(t);

    //     oos.writeObject(next);
    // }

    // private void readObject(ObjectInputStream ois) 
    // throws ClassNotFoundException, IOException {
    //     ois.defaultReadObject();
    //     time = (double)ois.readObject();
    //     joys = (double[])ois.readObject();

    //     // butts = (boolean[])ois.readObject();
    //     int l = (int)ois.readObject();
    //     butts = new boolean[l];
    //     int t = (int)ois.readObject();
    //     for (int i=l-1;i>=0;i--) {
    //         butts[i] = (t & (1 << i)) != 0;
    //     }

    //     next = (Saved)ois.readObject();
    // }
}