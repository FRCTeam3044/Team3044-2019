package frc.reference;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/*
    TalonEncoderPIDSource.java 
    This class wraps a WPI_TalonSRX so that it can be used as a PIDSource   
*/
public class TalonEncoderPIDSource  implements PIDSource // Interface edu.wpi.first.wpilibj.PIDSource
{
    private WPI_TalonSRX _sourceTalon ; 
    private PIDSourceType _sourceType=PIDSourceType.kRate;      // Default type, should be set by caller if needed 

    public TalonEncoderPIDSource(WPI_TalonSRX sourceTalon)
    {
        this._sourceTalon = sourceTalon; 
    }

    public TalonEncoderPIDSource(WPI_TalonSRX sourceTalon, PIDSourceType sourceType )
    {
        this._sourceTalon = sourceTalon; 
        this._sourceType = sourceType; 
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
            _sourceType = pidSource;
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return _sourceType;
    }

    /* This is the method that reads the value and passes it to the consuming caller  */
    @Override
    public double pidGet() {
        return _sourceTalon.getSensorCollection().getQuadraturePosition();
	}
}