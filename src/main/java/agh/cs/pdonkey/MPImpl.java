package agh.cs.pdonkey;

/**
 * pDonkey
 * Created by luknw on 16.12.2016
 */

public class MPImpl implements MP {
    private final String name;

    public MPImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MPImpl mp = (MPImpl) o;

        return name.equals(mp.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
