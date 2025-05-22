package contrib.components;

import core.Component;

public final class SprintComponent implements Component {

    private Boolean _isSprinting;

    public Boolean IsSprinting() {
        return _isSprinting;
    }

    public void StartSprinting() {
        _isSprinting = true;
    }

    public void StopSprinting() {
        _isSprinting = false;
    }
}
