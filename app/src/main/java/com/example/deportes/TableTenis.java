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

public class TableTenis extends AppCompatActivity {

    private String[] videoUrls = {
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733225067/table_tenis/r7uyvbipixdaduwpzlyl.mp4", //footwork
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733226231/table_tenis/dmukasdq9oafvstj7u3h.mp4", //coordination
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733225385/table_tenis/en7bxnl0lyjfdprgl7kb.mp4", //serve
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733225314/table_tenis/rbeyurkbeux9tpdlgg5x.mp4", //chop
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733225628/table_tenis/jljavgk0wuzuxk7a6hnp.mp4", //grip and stance
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733225646/table_tenis/rxt7sdire9idjddfcxdn.mp4", //spin
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733225836/table_tenis/zqcngtcscb8o5ccohqfq.mp4", //stroke
            "https://res.cloudinary.com/doxwvdotv/video/upload/v1733226032/table_tenis/jxfkd9odopmnfyfhifl5.mp4" //timing
    };

    private String[] videoTexts = {
            "To learn proper footwork in table tennis, follow these key steps:\n" +
                    "\n" +
                    "1. Understand the Importance of Footwork:\n" +
                    "\n" +
                    "• Footwork is essential for positioning yourself correctly to make effective shots. Good footwork helps you stay balanced, adjust quickly to the ball, and set up your next move.\n" +
                    "2. Work on Balance:\n" +
                    "\n" +
                    "• Always maintain a slightly bent posture with your knees flexed and your weight on the balls of your feet. This helps you react quickly to any shot.\n" +
                    "• Keep your body relaxed, and avoid locking your knees.\n" +
                    "3. Practice the Basic Stance:\n" +
                    "\n" +
                    "• Stand with your feet shoulder-width apart, with your body slightly turned toward the table.\n" +
                    "• Your non-dominant hand should be slightly forward to maintain balance and positioning.\n" +
                    "4. Master the Side-to-Side Movement:\n" +
                    "\n" +
                    "• Focus on moving quickly from side to side. Use small, quick steps rather than big strides to maintain balance and agility.\n" +
                    "• Practice moving to the left and right by pivoting on your inside foot, using your other foot to step quickly to the new position.\n" +
                    "5. Work on the Forehand and Backhand Steps:\n" +
                    "\n" +
                    "• For Forehand: Step forward with your dominant leg (right foot for right-handers, left foot for left-handers) as you strike the ball. This helps generate power and reach.\n" +
                    "• For Backhand: Use a similar movement, but step with the opposite leg. Keep your body facing the ball and ensure you’re balanced when executing the shot.\n" +
                    "6. Practice the Crossover Step:\n" +
                    "\n" +
                    "• When you need to move quickly to a corner of the table, use the crossover step (crossing one leg over the other while moving) to cover more ground efficiently.\n" +
                    "• This step is key when you're out of position and need to recover quickly.\n" +
                    "7. Learn the \"Step-and-Lunge\" Technique:\n" +
                    "\n" +
                    "• For quick, short-range shots, practice stepping forward with a lunge to close the gap between you and the ball while staying balanced.\n" +
                    "• Focus on keeping your body low when lunging to prevent losing your balance.\n" +
                    "8. Shadow Footwork Drills:\n" +
                    "\n" +
                    "• Without the ball, practice your footwork by imagining where the ball is coming from and moving accordingly. Perform side-to-side, forward, and backward movements.\n" +
                    "• These shadow drills help you develop muscle memory for the right positioning.\n" +
                    "9. Work on Recovery:\n" +
                    "\n" +
                    "• After every shot, work on quickly returning to a balanced position (center of the table, ready for the next shot). Quick recovery ensures you're always ready for the next ball.\n" +
                    "10. Include Footwork Drills in Your Routine:\n" +
                    "\n" +
                    "• Use footwork drills like \"side shuffle\" (side to side), \"front-back\" (moving in and out), and \"X-pattern\" (moving diagonally).\n" +
                    "• Incorporate these drills into your practice sessions for about 10–15 minutes each day.\n" +
                    "11. Gradually Increase Speed:\n" +
                    "• As you get more comfortable with footwork, gradually increase the speed and incorporate it into rally play with a partner or coach.\n" +
                    "12. Focus on Timing:\n" +
                    "• Footwork should be coordinated with the ball’s trajectory and speed. This means timing your steps according to the ball’s position to ensure you arrive at the right spot in time to make your shot.\n\n",

            "Improving coordination in table tennis requires a combination of physical conditioning, skill development, and practice. Here are the steps to help you learn coordination in table tennis:\n" +
                    "\n" +
                    "1. Develop Basic Motor Skills\n" +
                    "• Improving coordination in table tennis requires a combination of physical conditioning, skill development, and practice. Here are the steps to help you learn coordination in table tennis:\n" +
                    "\n" +
                    "1. Develop Basic Motor Skills\n" +
                    "• Footwork drills: Start with basic footwork drills like side steps, forward and backward movement, and pivoting. This helps you maintain proper positioning and move quickly.\n" +
                    "• Balance exercises: Practice standing on one leg or performing dynamic balance exercises. Good balance is essential for better coordination.\n" +
                    "2. Focus on Hand-Eye Coordination\n" +
                    "• Tracking the ball: Practice following the ball with your eyes throughout its entire trajectory. This improves focus and timing.\n" +
                    "• Ball bounce drills: Bounce a ball on the table with your racket while keeping your eyes on the ball. Try to control the bounce and keep it steady for a set amount of time.\n" +
                    "3. Work on Racket Control\n" +
                    "• Basic strokes: Start with simple forehand and backhand strokes, focusing on consistency and control.\n" +
                    "• Shadow play: Without the ball, practice your strokes and footwork in the air. Visualize hitting the ball and moving your body in sync with your racket.\n" +
                    "4. Improve Reaction Time\n" +
                    "• Fast ball drills: Have a partner feed you balls at various speeds and angles. Try to react as quickly as possible to each ball.\n" +
                    "• Multi-ball training: This involves a coach or partner feeding you multiple balls in quick succession, forcing you to respond quickly while maintaining coordination.\n" +
                    "5. Work on Timing and Rhythm\n" +
                    "• Rhythmic ball control: Hit the ball in a smooth, consistent rhythm. Focus on timing the ball’s bounce with your strokes.\n" +
                    "• Drill with variation: Practice receiving balls with varied speed and spin to challenge your timing and rhythm.\n" +
                    "6. Develop Left and Right Side Coordination\n" +
                    "• Alternating strokes: Practice using both your forehand and backhand with equal ease. This enhances your overall coordination and versatility.\n" +
                    "• Cross-court and down-the-line drills: Work on both diagonal and straight-line strokes, ensuring you can move and hit in any direction.\n" +
                    "7. Increase Reaction Speed with Multitasking\n" +
                    "• Serve and return drills: Focus on the coordination between your serve and your return. Serve with a variety of spins, speeds, and placements, and practice responding appropriately.\n" +
                    "• Simulated match drills: Have your partner throw a series of unpredictable balls at you, forcing you to react quickly and adapt.\n" +
                    "8. Consistency and Progression\n" +
                    "• Start slowly: Initially, focus on making accurate, controlled hits before speeding up the game.\n" +
                    "• Gradual difficulty increase: As you improve, increase the speed of the drills, the complexity of your strokes, and the variety of ball placements.\n" +
                    "9. Mental Focus and Visualization\n" +
                    "• Mental training: Visualize your movements and strokes before performing them. This mental practice can enhance physical coordination.\n" +
                    "• Stay focused: Train yourself to focus on the ball and anticipate its movement. Keeping your mind in sync with your body movements will improve coordination.\n" +
                    "10. Consistency Through Practice\n" +
                    "• Regular practice: Consistent, focused practice is key to developing coordination in table tennis. The more you practice, the more naturally the movements will come.\n" +
                    "• Feedback: Ask for feedback from a coach or experienced player to identify areas of improvement and focus on them during practice.Footwork drills: Start with basic footwork drills like side steps, forward and backward movement, and pivoting. This helps you maintain proper positioning and move quickly.\n" +
                    "• Balance exercises: Practice standing on one leg or performing dynamic balance exercises. Good balance is essential for better coordination.\n" +
                    "2. Focus on Hand-Eye Coordination\n" +
                    "• Tracking the ball: Practice following the ball with your eyes throughout its entire trajectory. This improves focus and timing.\n" +
                    "• Ball bounce drills: Bounce a ball on the table with your racket while keeping your eyes on the ball. Try to control the bounce and keep it steady for a set amount of time.\n" +
                    "3. Work on Racket Control\n" +
                    "• Basic strokes: Start with simple forehand and backhand strokes, focusing on consistency and control.\n" +
                    "• Shadow play: Without the ball, practice your strokes and footwork in the air. Visualize hitting the ball and moving your body in sync with your racket.\n" +
                    "4. Improve Reaction Time\n" +
                    "• Fast ball drills: Have a partner feed you balls at various speeds and angles. Try to react as quickly as possible to each ball.\n" +
                    "• Multi-ball training: This involves a coach or partner feeding you multiple balls in quick succession, forcing you to respond quickly while maintaining coordination.\n" +
                    "5. Work on Timing and Rhythm\n" +
                    "• Rhythmic ball control: Hit the ball in a smooth, consistent rhythm. Focus on timing the ball’s bounce with your strokes.\n" +
                    "• Drill with variation: Practice receiving balls with varied speed and spin to challenge your timing and rhythm.\n" +
                    "6. Develop Left and Right Side Coordination\n" +
                    "• Alternating strokes: Practice using both your forehand and backhand with equal ease. This enhances your overall coordination and versatility.\n" +
                    "• Cross-court and down-the-line drills: Work on both diagonal and straight-line strokes, ensuring you can move and hit in any direction.\n" +
                    "7. Increase Reaction Speed with Multitasking\n" +
                    "• Serve and return drills: Focus on the coordination between your serve and your return. Serve with a variety of spins, speeds, and placements, and practice responding appropriately.\n" +
                    "• Simulated match drills: Have your partner throw a series of unpredictable balls at you, forcing you to react quickly and adapt.\n" +
                    "8. Consistency and Progression\n" +
                    "Start slowly: Initially, focus on making accurate, controlled hits before speeding up the game.\n" +
                    "Gradual difficulty increase: As you improve, increase the speed of the drills, the complexity of your strokes, and the variety of ball placements.\n" +
                    "9. Mental Focus and Visualization\n" +
                    "• Mental training: Visualize your movements and strokes before performing them. This mental practice can enhance physical coordination.\n" +
                    "• Stay focused: Train yourself to focus on the ball and anticipate its movement. Keeping your mind in sync with your body movements will improve coordination.\n" +
                    "10. Consistency Through Practice\n" +
                    "• Regular practice: Consistent, focused practice is key to developing coordination in table tennis. The more you practice, the more naturally the movements will come.\n" +
                    "• Feedback: Ask for feedback from a coach or experienced player to identify areas of improvement and focus on them during practice.\n\n",
            "Here are the short steps to learn how to serve in table tennis:\n" +
                    "\n" +
                    "1. Grip the Paddle: Hold the racket with a relaxed grip, using a shakehand or penhold grip.\n" +
                    "\n" +
                    "2. Position Your Body: Stand behind the end line of the table with your feet shoulder-width apart. Keep your body slightly angled towards the table.\n" +
                    "\n" +
                    "3. Ball Placement: Hold the ball in your non-dominant hand above the table. The ball must be behind the end line and above the playing surface when you serve.\n" +
                    "\n" +
                    "4. Toss the Ball: Toss the ball vertically at least 6 inches (16 cm) into the air. Keep it straight and without spin.\n" +
                    "\n" +
                    "5. Contact the Ball: As the ball descends, hit it with the racket. You must strike the ball so that it bounces once on your side and then on the opponent’s side.\n" +
                    "\n" +
                    "6. Follow Through: After hitting the ball, follow through with your racket and be prepared for the next shot.\n" +
                    "\n" +
                    "7. Practice Different Spins: Once you are comfortable with the basic serve, practice adding topspin, backspin, or sidespin for variety.\n\n",

            "To learn to chop in table tennis, follow these steps:\n" +
                    "\n" +
                    "1. Grip: Use a standard shakehand or penhold grip. Keep your grip relaxed for better control.\n" +
                    "\n" +
                    "2. Positioning: Stand with your feet shoulder-width apart, knees slightly bent, and body leaning forward.\n" +
                    "\n" +
                    "3. Backswing: Hold the racket low and behind you. Rotate your body slightly for power.\n" +
                    "\n" +
                    "4. Contact the Ball: As the ball comes toward you, lower your racket to meet the ball with the backspin side of the racket, using a brushing motion. Keep the racket angle slightly open (tilted back).\n" +
                    "\n" +
                    "5. Follow-through: After hitting the ball, let your racket swing upward slightly for a controlled and consistent chop.\n" +
                    "\n" +
                    "6. Practice: Focus on brushing the ball lightly to generate backspin, rather than hitting it hard. Work on controlling the depth and placement of your shots.\n" +
                    "\n" +
                    "7. Footwork: Keep adjusting your position to ensure your racket is in the correct place to execute the chop.\n\n",
            "To learn the grip in table tennis, follow these short steps:\n" +
                    "\n" +
                    "1. Choose a Grip Style:\n" +
                    "\n" +
                    "   Shakehand Grip: Hold the racket like you're shaking hands with it, with your index finger placed along the edge and the thumb on the back.\n" +
                    "   Penhold Grip: Hold the racket like a pen, with your thumb and index finger forming a V-shape around the handle.\n\n" +
                    "2. Practice the Basic Hold:\n" +
                    "\n" +
                    "   For shakehand, keep your fingers relaxed around the handle, and make sure your grip is firm but not too tight.\n" +
                    "   For penhold, your thumb and index finger should wrap around the handle with your other fingers resting on the back of the racket.\n\n" +
                    "3. Check the Comfort: Ensure the grip feels comfortable and stable, allowing you to move the racket freely.\n" +
                    "\n" +
                    "   Adjust Your Grip for Play: While playing, adjust your grip for different shots (forehand, backhand) but keep your hand relaxed to maintain control.\n" +
                    "\n" +
                    "4. Practice: Experiment with both grips while playing, refining the feel and control as you practice.\n" +
                    "\n",

            "Here are the short steps to learn how to spin in table tennis:\n" +
                    "\n" +
                    "1. Understand the Types of Spin:\n" +
                    "\n" +
                    "   Topspin: Ball moves forward and upward.\n" +
                    "   Backspin: Ball moves backward and downward.\n" +
                    "   Sidespin: Ball curves to the left or right.\n" +
                    "2. Grip: Hold the racket properly with a relaxed grip. For most spins, a shakehand grip works best.\n" +
                    "\n" +
                    "3. Stance and Footwork: Position yourself correctly and be ready to move. Your body should face sideways with knees slightly bent for better balance.\n" +
                    "\n" +
                    "4. Contact Angle:\n" +
                    "\n" +
                    "   For topspin, angle the racket slightly upward and brush the ball from bottom to top.\n" +
                    "   For backspin, angle the racket slightly downward and brush the ball from top to bottom.\n" +
                    "   For sidespin, tilt the racket sideways and brush the ball in a sideways motion (left or right).\n" +
                    "5. Practice with the Wall or Partner: Start slow with a wall or a partner, focusing on generating spin by brushing the ball at different angles.\n" +
                    "\n" +
                    "6. Consistency and Control: Focus on controlling the spin rather than power. As you become more consistent, you can increase the speed and intensity.\n" +
                    "\n" +
                    "7. Use Different Rackets: Try different rubber types (e.g., sticky, tacky) to see how they affect the spin.\n" +
                    "\n" +
                    "8. Adjust Your Timing: Hit the ball at its peak or as it’s rising to get more spin.\n\n",

            "To learn strokes in table tennis, follow these short steps:\n" +
                    "\n" +
                    "1. Grip the Paddle: Hold the racket with a firm but relaxed grip. Use the shakehand or penhold grip depending on preference.\n" +
                    "\n" +
                    "2. Master the Basic Strokes:\n" +
                    "\n" +
                    "   Forehand Drive: Stand side-on, with knees slightly bent, and hit the ball with the racket facing forward.\n" +
                    "   Backhand Drive: Position yourself facing the table and hit the ball with the back of the racket facing forward.\n\n" +
                    "3. Practice the Push:\n" +
                    "\n" +
                    "   Forehand Push: Use a shorter stroke, keeping the racket angle slightly open, to return backspin.\n" +
                    "   Backhand Push: Similar to forehand but using the backhand side to return backspin.\n\n" +
                    "4. Learn the Loop:\n" +
                    "\n" +
                    "   For topspin, use a brushing motion with your racket to generate spin while hitting the ball.\n" +
                    "5. Work on the Serve: Experiment with different types of serves (topspin, backspin, side spin) to make it harder for your opponent to return.\n" +
                    "\n" +
                    "6. Focus on Footwork: Practice moving quickly to position yourself properly for different shots.\n" +
                    "\n" +
                    "7. Practice Regularly: Consistent practice will improve your technique and reflexes over time.\n\n",
            "Here are short steps to help you learn timing in table tennis:\n" +
                    "\n" +
                    "1. Focus on the Ball: Always keep your eyes on the ball to anticipate its speed and spin. Track its movement closely as it approaches your side of the table.\n" +
                    "\n" +
                    "2. Anticipate the Bounce: Learn to judge where and when the ball will bounce on your side of the table. This helps you position yourself early for an effective return.\n" +
                    "\n" +
                    "3. Footwork: Develop quick footwork so you can move into the right position for each shot. Good positioning ensures you can meet the ball at the right time.\n" +
                    "\n" +
                    "4. Adjust to Speed: Practice adjusting your stroke based on the speed of the ball. If the ball is fast, shorten your stroke and react quickly; if it's slow, you can take more time to prepare your shot.\n" +
                    "\n" +
                    "5. Practice with Varying Balls: Play with different players who have varying speeds and styles. This helps you develop a sense of timing with different types of shots.\n" +
                    "\n" +
                    "6. Shadow Practice: Without a ball, practice your strokes to improve the rhythm and timing of your movements.\n" +
                    "\n" +
                    "7. Consistent Drills: Engage in consistent drills that emphasize timing, such as fast rallies, counter-hits, and reaction exercises.\n\n"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table_tenis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton footwork, coordination, serve, chop, grid_stance, spin, stroke, timing;

        footwork = findViewById(R.id.table_tenis_footwork);
        footwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(0);
            }
        });

        coordination = findViewById(R.id.table_tenis_coordination);
        coordination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(1);
            }
        });

        serve = findViewById(R.id.table_tenis_serve);
        serve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(2);
            }
        });

        chop = findViewById(R.id.table_tenis_chop);
        chop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(3);
            }
        });

        grid_stance = findViewById(R.id.table_tenis_gripandstance);
        grid_stance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(4);
            }
        });

        spin = findViewById(R.id.table_tenis_spin);
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(5);
            }
        });

        stroke = findViewById(R.id.table_tenis_stroke);
        stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(5);
            }
        });

        timing = findViewById(R.id.table_tenis_timing);
        timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(5);
            }
        });
    }

    private void playVideo(int index)
    {
        if (index >= 0 && index < videoUrls.length)
        {
            Intent intent = new Intent(TableTenis.this, Video.class);
            Uri videoUri = Uri.parse(videoUrls[index]);
            intent.putExtra("videoUri", videoUri.toString());
            intent.putExtra("videoText", videoTexts[index]);
            startActivity(intent);
        }
    }
}