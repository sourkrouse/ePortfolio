package com.zybooks.weighttracker.data.DAO;

        import androidx.room.Dao;
        import androidx.room.Delete;
        import androidx.room.Insert;
        import androidx.room.OnConflictStrategy;
        import androidx.room.Query;
        import androidx.room.Update;

        import com.zybooks.weighttracker.data.model.Weights;
        import com.zybooks.weighttracker.data.DbConfig;

        import java.util.List;


@Dao
public interface WeightsDao {

    @Query("SELECT * FROM DailyWeights WHERE Rid = :id")
    public Weights getWeight(long id);

    @Query("SELECT * FROM DailyWeights ORDER BY Rid DESC")
    public List<Weights> getWeights();

    @Query("SELECT * FROM DailyWeights ORDER BY record_date DESC")
    public List<Weights> getWeightsNewerFirst();

    @Query("SELECT * FROM DailyWeights ORDER BY record_date ASC")
    public List<Weights> getWeightsOlderFirst();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertWeight(Weights weights);

    @Update
    public void updateWeight(Weights weights);

    @Delete
    public void deleteWeight(Weights weights);


}