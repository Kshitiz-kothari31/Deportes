package com.example.deportes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Volleyball extends AppCompatActivity {

    private  String[] videoUrls = {
            "",
            "",
            ""
    };

    private String[] videoTexts = {
            "Here are the short steps to learn serving in volleyball:\n" +
                    "\n" +
                    "1. Position Yourself: Stand behind the baseline with your feet shoulder-width apart. Hold the ball in your non-dominant hand and position it at waist height.\n" +
                    "\n" +
                    "2. Grip the Ball: Use your dominant hand to grip the ball, keeping your fingers relaxed and spread out. Hold the ball firmly but not too tight.\n" +
                    "\n" +
                    "3. Throw the Ball Up: Toss the ball gently in front of you, around 6-12 inches. Keep your toss consistent and at a comfortable height.\n" +
                    "\n" +
                    "4. Prepare to Hit: Step forward with your non-dominant foot, and rotate your body so that your dominant arm is back, ready to swing.\n" +
                    "\n" +
                    "5. Strike the Ball: Swing your dominant arm forward and hit the ball with the heel of your hand. Aim for a consistent, controlled hit.\n" +
                    "\n" +
                    "6. Follow Through: Let your arm naturally follow through the motion, ensuring your body stays balanced and you're prepared for the next play.\n" +
                    "\n" +
                    "7. Practice: Focus on consistency with your toss and strike to improve accuracy and power.\n" +
                    "\n" +
                    "Regular practice will help refine your technique over time!\n\n",

            "Here are the short steps to improve decision-making in volleyball:\n" +
                    "\n" +
                    "1. Understand the Game: Learn the basic rules, positions, and strategies to recognize the flow of the game.\n" +
                    "\n" +
                    "2. Anticipate the Play: Watch the setter and the opposing team’s movements to predict where the ball might go.\n" +
                    "\n" +
                    "3. Read the Defense: Quickly analyze the opponents’ positioning, and identify weaknesses or gaps in their defense.\n" +
                    "\n" +
                    "4. Improve Communication: Talk with teammates to share information, such as who’s going for the ball or calling out plays.\n" +
                    "\n" +
                    "5. Focus on the Ball: Stay focused on the ball to make faster and more accurate decisions.\n" +
                    "\n" +
                    "6. Be Flexible: Stay adaptable to changing game situations and adjust your approach as needed.\n" +
                    "\n" +
                    "7. Practice Under Pressure: Simulate game-like situations in practice to build confidence in making quick decisions.\n" +
                    "\n" +
                    "8. Review and Reflect: After each game, reflect on your decisions to see what worked and what could be improved.\n" +
                    "\n" +
                    "These steps will help you improve your decision-making skills during a volleyball match! \n\n",

            "Here are some short steps to learn rotation in volleyball:\n" +
                    "\n" +
                    "1. Understand the Court Setup: Learn the six positions on the court (Front Left, Front Center, Front Right, Back Left, Back Center, and Back Right).\n" +
                    "\n" +
                    "2. Know the Rotation Order: Players rotate in a clockwise direction after winning a point when serving. The first rotation is from the serving position (Back Right) to Front Right.\n" +
                    "\n" +
                    "3. Serve from the Correct Position: Start in the Back Right position and serve when it's your turn. After serving, rotate to the next position.\n" +
                    "\n" +
                    "4. Focus on Player Movements: As the ball is served, players must move into their designated positions. Front row players move forward, and back row players move back.\n" +
                    "\n" +
                    "5. Stay Aware of the Rotation: Keep track of when your turn to serve comes up and which position you need to be in next. Avoid stepping into the wrong position, as that can lead to a rotation fault.\n" +
                    "\n" +
                    "6. Practice with Team: Rotate with your teammates in scrimmages and games to get comfortable with the timing and movement.\n" +
                    "\n" +
                    "Repetition and awareness during practice will help make rotations become second nature!\n\n",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_volleyball);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton v_serving, v_decision_making, v_rotation;

        v_serving = findViewById(R.id.volleyball_serving);
        v_serving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(0);
            }
        });

        v_decision_making = findViewById(R.id.volleyball_decision_making);
        v_decision_making.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(1);
            }
        });

        v_rotation = findViewById(R.id.volleyball_rotation);
        v_rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(2);
            }
        });
    }

    private void playVideo(int index)
    {
        if(index >= 0 && index < videoUrls.length)
        {
            Intent intent = new Intent(Volleyball.this, Video.class);
            Uri videoUri = Uri.parse(videoUrls[index]);
            intent.putExtra("videoUri", videoUri.toString());
            intent.putExtra("videoText", videoTexts[index]);
            startActivity(intent);
        }
    }

}