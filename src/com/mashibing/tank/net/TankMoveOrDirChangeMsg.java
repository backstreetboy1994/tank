package com.mashibing.tank.net;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Tank;
import com.mashibing.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankMoveOrDirChangeMsg extends Msg {
    private UUID id;
    private int x, y;
    private Dir dir;

    public TankMoveOrDirChangeMsg(UUID id, int x, int y, Dir dir) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public TankMoveOrDirChangeMsg() {
    }

    public UUID getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Dir getDir() {
        return dir;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;

        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dos != null)
                    dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));

        try{
            this.id = new UUID(dis.readLong(), dis.readLong());
            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.values()[dis.readInt()];
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void handle() {
        if (this.id.equals(TankFrame.INSTANCE.getGm().getMyTank().getId()))
            return;
        Tank t = TankFrame.INSTANCE.getGm().findTankByUUID(this.id);

        if (t != null){
            t.setMoving(true);
            t.setX(this.x);
            t.setY(this.y);
            t.setDir(this.dir);
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.TankMoveOrDirChange;
    }

    @Override
    public String toString() {
        return "TankMoveOrDirChangeMsg{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                '}';
    }
}
