package com.example.deportes2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class fragment_Basketball_videos extends Fragment {

    private String[] videoTexts = {
            "Learning how to pass effectively in basketball involves understanding the different types of passes, practicing the correct technique, and knowing when to use each pass during a game. Here’s a step-by-step guide:\n\n" +
                    "1. Understand the Basics of Passing\n\n" +
                    "   • Purpose of passing: Move the ball quickly and accurately to a teammate to maintain possession and create scoring opportunities.\n" +
                    "   • Key elements: Accuracy, speed, timing, and communication.\n\n" +
                    "2. Learn the Types of Passes\n\n" +
                    "   a. Practice each pass individually:\n\n" +
                    "\n" +
                    "      • Chest Pass: Hold the ball at chest height, step forward, and push the ball directly to your teammate’s chest with a flick of your wrists.\n" +
                    "      • Bounce Pass: Similar to the chest pass, but aim the ball to bounce about two-thirds of the way to your teammate.\n" +
                    "      • Overhead Pass: Hold the ball above your head and throw it in a straight line to your teammate, using your wrists for control.\n" +
                    "      • One-Hand Push Pass: Use one hand to pass the ball in tight spaces, especially on the move.\n" +
                    "      • Behind-the-Back Pass: Advanced pass for misdirection. Practice control before using in games.\n\n" +
                    "3. Work on Passing Technique\n\n" +
                    "   • Grip: Hold the ball firmly but not too tightly.\n" +
                    "   • Footwork: Step into the pass with your lead foot for more power and accuracy.\n" +
                    "   • Follow-Through: Snap your wrists and point your fingers toward your target to guide the ball.\n\n" +
                    "4. Practice Passing Drills\n\n" +
                    "   • Wall Passing: Stand a few feet from a wall and pass the ball to practice accuracy and speed.\n" +
                    "   • Partner Passing: Pair with a teammate and practice various passes, focusing on technique and communication.\n" +
                    "   • Dynamic Passing: Incorporate movement, such as running or dribbling, before passing.\n" +
                    "   • Triangle Drill: Set up with three players in a triangle and pass the ball around, emphasizing quick decision-making.\n\n" +
                    "5. Develop Game Awareness\n\n" +
                    "   • Read the defense: Recognize when and where to pass to avoid turnovers.\n" +
                    "   • Communicate: Use verbal and non-verbal signals to coordinate with teammates.\n" +
                    "   • Spacing: Ensure teammates are in optimal positions to receive the pass.\n\n" +
                    "6. Incorporate Passing into Gameplay\n\n" +
                    "   • Play small-sided games (e.g., 3-on-3) to practice passing under pressure.\n" +
                    "   • Focus on quick ball movement to avoid holding the ball for too long.\n\n" +
                    "7. Seek Feedback and Adjust\n\n" +
                    "   • Ask coaches or teammates for feedback on your passing.\n" +
                    "   • Watch game footage to analyze your decision-making and technique.\n\n" +
                    "8. Stay Consistent\n\n" +
                    "   • Dedicate time to passing drills during every practice session.\n" +
                    "   • Practice against different levels of defensive pressure.\n\n" +
                    "Mastery comes with repetition, so keep practicing and challenging yourself in game-like scenarios!\n\n",

            "Learning how to shoot effectively in basketball involves mastering technique, consistency, and practice. Here’s a step-by-step guide to help you develop your shooting skills:\n\n" +
                    "\n" +
                    "Step 1: Understand the Fundamentals\n\n" +
                    "  Stance and Balance" +
                    "\n" +
                    "   • Keep your feet shoulder-width apart for stability.\n" +
                    "   • Bend your knees slightly to stay balanced and ready to jump.\n" +
                    "   • Position your shooting foot slightly ahead of your non-shooting foot (right foot for right-handed shooters, left for left-handed).\n" +
                    "  Grip the Ball Correctly\n" +
                    "\n" +
                    "   • Use your dominant hand (shooting hand) to hold the ball under its center.\n" +
                    "   • Place your non-shooting hand on the side of the ball for support.\n" +
                    "   • Keep your fingers spread apart and avoid using your palm to hold the ball.\n" +
                    "  Eye Focus\n" +
                    "\n" +
                    "   • Aim for the back of the rim or the center of the hoop, depending on what works best for you.\n\n" +
                    "Step 2: Practice Shooting Form\n\n" +
                    "  Shooting Pocket\n" +
                    "\n" +
                    "   • Hold the ball at chest level, close to your body.\n" +
                    "   • Ensure your shooting elbow is aligned under the ball and forms a 90-degree angle.\n" +
                    "   • Release and Follow-Through\n" +
                    "\n" +
                    "   • Extend your shooting arm upwards in a smooth motion.\n" +
                    "   • Flick your wrist to give the ball backspin.\n" +
                    "   • Hold your follow-through, with your fingers pointing toward the basket and your hand in a \"gooseneck\" position.\n" +
                    "   • Arc and Power\n" +
                    "\n" +
                    "   • Use your legs to generate power, especially for longer shots.\n" +
                    "   • Aim for a high arc, as it increases your chances of the ball going through the hoop.\n\n" +
                    "Step 3: Start Close to the Basket\n\n" +
                    "   • Begin with layups and short-range shots to build confidence and refine your technique.\n" +
                    "   • Gradually move further away as your accuracy improves.\n\n" +
                    "Step 4: Practice Shooting Drills\n\n" +
                    "  Spot Shooting\n" +
                    "\n" +
                    "   • Shoot from various spots on the court (baseline, free throw, three-point line).\n" +
                    "   • Free Throws\n" +
                    "\n" +
                    "   • Practice free throws to develop consistency and focus.\n" +
                    "   • Catch-and-Shoot\n" +
                    "\n" +
                    "   • Practice receiving a pass and quickly shooting without hesitation.\n" +
                    "   • Off-the-Dribble Shooting\n" +
                    "\n" +
                    "   • Work on dribbling and pulling up for a shot.\n" +
                    "   • Game Scenarios\n" +
                    "\n" +
                    "   • Simulate game situations to improve shooting under pressure.\n\n" +
                    "Step 5: Work on Consistency\n\n" +
                    "  Repetition\n" +
                    "\n" +
                    "   • Practice daily to develop muscle memory.\n" +
                    "   • Take 200-300 shots per practice session if possible.\n" +
                    "  Tracking Progress\n" +
                    "\n" +
                    "   • Keep track of your shooting percentage to measure improvement.\n" +
                    "   • Video Analysis\n" +
                    "\n" +
                    "   • Record yourself shooting and analyze your form.\n\n" +
                    "Step 6: Strengthen Related Skills\n\n" +
                    "  Footwork\n\n" +
                    "   • Practice proper foot placement for quick and balanced shooting.\n\n" +
                    "  Core Strength\n\n" +
                    "   • Engage in exercises like planks and squats to enhance balance and stability.\n\n" +
                    "  Mental Toughness\n\n" +
                    "   • Stay confident, especially during games. Visualization and mindfulness can help.\n\n" +
                    "Step 7: Learn from Experts\n\n" +
                    "   • Watch professional players to study their shooting techniques.\n" +
                    "   • Attend basketball camps or work with a coach for personalized feedback.\n\n" +
                    "Step 8: Practice Regularly\n\n" +
                    "   • Stay patient and consistent. Improvement takes time and effort.\n" +
                    "   • Gradually incorporate advanced moves like fadeaways and step-back shots into your training.\n\n" +
                    "Good luck, and keep practicing!\n\n",

            "Learning to dribble effectively in basketball requires mastering technique, developing control, and practicing consistently. Here’s a step-by-step guide to improve your dribbling skills:\n" +

                    "1. Understand the Basics\n\n" +
                    "   • Keep Your Head Up: Always look forward, not at the ball. This allows you to see the court and anticipate plays.\n" +
                    "   • Stay Low: Bend your knees slightly to maintain a low center of gravity for better balance and quicker movements.\n" +
                    "   • Use Your Fingertips: Avoid using your palm to control the ball. Dribble with your fingertips for more precision and control.\n\n" +
                    "2. Practice Stationary Dribbling\n\n" +
                    "   Start in a comfortable position and focus on control.\n" +
                    "\n" +
                    "     • Right-Hand Dribble: Dribble the ball with your right hand at waist height.\n" +
                    "     • Left-Hand Dribble: Repeat with your left hand.\n" +
                    "     • Alternating Hands: Switch the ball between hands in a steady rhythm.\n" +
                    "     • Low Dribbles: Dribble the ball closer to the ground to improve control.\n\n" +
                    "3. Add Movement\n\n" +
                    "   Once you’re comfortable stationary, incorporate motion.\n" +
                    "\n" +
                    "     • Walking Dribble: Walk slowly while dribbling with one hand, then alternate hands.\n" +
                    "     • Jogging Dribble: Gradually increase speed to a jog, maintaining control.\n" +
                    "     • Crossover Dribble: Practice crossing the ball from one hand to the other while moving.\n\n" +
                    "4. Incorporate Advanced Techniques\n\n" +
                    "   • Between the Legs: Dribble the ball between your legs while maintaining control.\n" +
                    "   • Behind the Back: Move the ball behind your back from one hand to the other.\n" +
                    "   • Spin Move: Dribble, spin your body, and switch hands to change direction.\n" +
                    "   • Hesitation Dribble: Slow down briefly, then accelerate to fake out defenders.\n\n" +
                    "5. Dribbling Drills\n\n" +
                    "   • Cone Weaving: Set up cones and dribble around them to improve handling and footwork.\n" +
                    "   • Figure Eight: Dribble the ball in a figure-eight motion between your legs.\n" +
                    "   • Two-Ball Dribble: Dribble two balls simultaneously to improve coordination.\n\n" +
                    "6. Build Speed and Agility\n\n" +
                    "   • Use resistance bands or work on agility ladders to strengthen your legs and improve foot speed.\n" +
                    "   • Practice dribbling at game speed to simulate real scenarios.\n\n" +
                    "7. Play in Real Scenarios\n\n" +
                    "   • Join pickup games to practice dribbling against defenders.\n" +
                    "   • Challenge yourself to dribble under pressure during games or scrimmages.\n\n" +
                    "8. Stay Consistent and Track Progress\n\n" +
                    "   • Dedicate at least 15–30 minutes daily to dribbling drills.\n\n" +
                    "Record your sessions to identify areas for improvement.\n\n",

            "Learning footwork in basketball is essential for becoming a more versatile and effective player. Here’s a step-by-step guide to help you improve your footwork:\n" +

                    "1. Understand the Basics\n\n" +
                    "   • Basketball Stance: Start with a balanced, athletic stance—feet shoulder-width apart, knees slightly bent, weight on the balls of your feet.\n" +
                    "   • Pivoting: Learn the concept of the pivot foot. Once you plant one foot, it stays in place while the other foot can move freely.\n" +
                    "   • Triple-Threat Position: Practice holding the ball in a position where you can shoot, pass, or dribble.\n\n" +
                    "2. Work on Agility and Balance\n\n" +
                    "   • Agility Drills: Use ladders, cones, or jump ropes to improve foot speed and coordination.\n" +
                    "   • Balance Exercises: Incorporate single-leg squats and balance board drills to maintain control during movements.\n\n" +
                    "3. Learn Basic Footwork Moves\n\n" +
                    "   • Jab Step: Step forward quickly with one foot to fake a move, then return to your original position.\n" +
                    "   • Drop Step: Use this move in the post. Step back with one foot to position yourself closer to the basket.\n" +
                    "   • Crossover Step: Step across your body with one foot to change direction quickly.\n\n" +
                    "4. Practice Defensive Footwork\n\n" +
                    "   • Defensive Slides: Stay low, keep your weight on the balls of your feet, and move laterally without crossing your feet.\n" +
                    "   • Closeouts: Sprint toward a shooter, then slow down and get into a defensive stance to contest the shot without fouling.\n\n" +
                    "5. Master Advanced Moves\n\n" +
                    "   • Euro Step: Take two long, angled steps to avoid a defender and finish at the rim.\n" +
                    "   • Spin Move: Use a quick 180-degree spin to change direction while dribbling.\n" +
                    "   • Step-Back: Create space for a jump shot by stepping back quickly after a dribble.\n\n" +
                    "6. Drills to Reinforce Footwork\n\n" +
                    "   • Cone Drills: Set up cones to simulate defenders and practice dribbling and pivoting around them.\n" +
                    "   • Shadow Drills: Work with a partner who mimics your movements to practice offensive and defensive footwork.\n" +
                    "   • Layup Progression: Start with slow-motion steps to perfect layup footwork, then speed up.\n\n" +
                    "7. Game Situations\n\n" +
                    "   • 1-on-1 Games: Apply your footwork in competitive scenarios to learn how it works under pressure.\n" +
                    "   • Scrimmages: Incorporate footwork skills into game-like situations to build confidence and familiarity.\n\n" +
                    "8. Film and Analyze\n\n" +
                    "   • Record yourself practicing and playing. Analyze your movements to spot areas for improvement.\n" +
                    "   • Watch professional players to study how they use footwork in different situations.\n\n" +
                    "9. Consistency and Repetition\n\n" +
                    "   • Dedicate time daily to footwork drills. The more you practice, the more natural it will become.\n" +
                    "10. Get Feedback\n\n" +
                    "   • Work with a coach or experienced player to refine your technique and correct mistakes.\n\n",

            "To learn how to run effectively in basketball, follow these steps:\n" +
                    "\n" +
                    "1. Develop Proper Running Form\n\n" +
                    "   • Posture: Stay upright with a slight lean forward from the ankles (not the waist). Keep your head up to see the court.\n" +
                    "   • Arm Movement: Use your arms to help propel your body forward. Keep them bent at about 90 degrees and swing them naturally.\n\n" +
                    "   • Stride: Focus on shorter, quicker strides for better agility and to maintain balance. Aim for a smooth, fluid motion.\n\n" +
                    "2. Work on Speed and Agility\n\n" +
                    "   • Sprints: Practice sprints, focusing on quick starts and stops. Basketball requires fast, explosive movements, so incorporate sprinting drills.\n" +
                    "   • Agility Drills: Incorporate lateral movements and change of direction (e.g., shuttle runs, ladder drills) to improve agility for both offense and defense.\n\n" +
                    "3. Learn to Run with the Ball\n\n" +
                    "   • Dribbling while Running: Start dribbling at a slow pace, then gradually increase your speed. Practice dribbling with both hands and maintain control while running.\n" +
                    "   • Head Up: Always keep your head up to scan the court for teammates and opponents. This helps you make better decisions while running.\n\n" +
                    "4. Conditioning\n\n" +
                    "   • Endurance: Basketball involves a lot of quick bursts, so work on your cardiovascular fitness through running, cycling, or interval training.\n" +
                    "   • Strength: Build leg strength to improve your speed and endurance, incorporating exercises like squats, lunges, and plyometrics.\n\n" +
                    "5. Game-Specific Running\n\n" +
                    "   • Fast Breaks: Practice running at full speed on a fast break, transitioning from defense to offense. Focus on timing your run and making quick decisions (pass, shoot, or drive).\n" +
                    "   • Defensive Running: Learn how to quickly transition between offensive and defensive positions. Work on running in defensive stances to stay low and react to movements.\n\n" +
                    "6. Improve Footwork\n\n" +
                    "   • Footwork Drills: Incorporate jump rope, cone drills, and other agility drills to enhance your footwork. Quick feet will help you maintain balance while running.\n\n" +
                    "7. Play in Games\n\n" +
                    "   • Experience: The best way to improve running in basketball is to play as often as possible. Experience in actual game situations will improve your running in context, like in transitions or chasing down fast breaks.\n\n",

            "To learn jumping in basketball effectively, follow these steps to build strength, coordination, and technique:\n" +
                    "\n" +
                    "1. Build Leg Strength\n\n" +
                    "   • Squats: Perform regular squats to strengthen your quads, hamstrings, and glutes.\n" +
                    "   • Lunges: Add lunges to target your legs and improve balance.\n" +
                    "   • Calf Raises: Strengthen your calves for better jump height.\n\n" +
                    "2. Work on Plyometrics\n\n" +
                    "   Plyometric exercises enhance explosive power, which is essential for jumping.\n\n" +
                    "\n" +
                    "   • Box Jumps: Jump onto a stable box or platform to develop explosive power.\n" +
                    "   • Broad Jumps: Perform long jumps from a standing position to build lower body power.\n" +
                    "   • Jump Rope: Jumping rope increases stamina and foot speed, which will also help your vertical jump.\n\n" +
                    "3. Jumping Technique\n\n" +
                    "   • Proper Form: When you jump, bend your knees, swing your arms back, and explode upwards using your legs and core for power.\n" +
                    "   • Use Your Arms: Incorporate a powerful arm swing as you jump. The motion of swinging your arms upwards helps propel you higher.\n\n" +
                    "4. Flexibility and Mobility\n\n" +
                    "   • Stretch your calves, hamstrings, quads, and hips regularly to increase flexibility, which is important for maximizing jump height and avoiding injury.\n\n" +
                    "5. Core Strength\n\n" +
                    "   • Work on exercises like planks and Russian twists to improve your core strength, which is essential for controlling your body while jumping and landing.\n\n" +
                    "6. Jumping Drills\n\n" +
                    "   • Vertical Jump Drill: Stand with your feet shoulder-width apart, squat slightly, then jump as high as possible, reaching for a target (like a basketball rim or a wall).\n" +
                    "   • Tuck Jumps: Jump as high as you can, bringing your knees up towards your chest. This will help improve your vertical jump and overall agility.\n\n" +
                    "7. Practice Timing and Rhythm\n\n" +
                    "   • In basketball, timing your jumps is crucial for rebounding and blocking shots.\n" +
                    "\n" +
                    "   • Jumping for Rebounds: Practice jumping at the right time to grab a rebound after a shot attempt.\n" +
                    "   • Block Shot Drills: Work on timing your jump to block shots by timing your leap just as the ball is released.\n\n" +
                    "8. Consistency\n\n" +
                    "   • Make jumping drills a regular part of your workout routine to steadily increase your vertical jump.\n\n" +
                    "With practice and consistent effort, you'll see improvements in your jumping ability and overall basketball performance.\n\n"
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basketball_videos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton b_passing, b_shooting, b_dribbling, b_footwork, b_running, b_jumping;

        b_passing = view.findViewById(R.id.basketball_passing);
        b_passing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("passing.mp4", videos);
            }
        });

        b_shooting = getView().findViewById(R.id.basketball_shooting);
        b_shooting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("shooting.mp4", videos);
            }
        });

        b_dribbling = getView().findViewById(R.id.basketball_dribbling);
        b_dribbling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("dribbling.mp4", videos);
            }
        });

        b_footwork = getView().findViewById(R.id.basketball_footwork);
        b_footwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("footwork.mp4", videos);
            }
        });

        b_running = getView().findViewById(R.id.basketball_running);
        b_running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("running.mp4", videos);
            }
        });

        b_jumping = getView().findViewById(R.id.basketball_jumping);
        b_jumping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("jumping.mp4", videos);
            }
        });
    }

    private void playVideoWithName(String videoName, List<String> videoUrls){
        for(String url : videoUrls){
            if(url.contains(videoName)){
                Intent intent = new Intent(requireContext(), VideoPlayer.class);
                intent.putExtra("actionName", url);
                startActivity(intent);
                return;
            }
        }

        Toast.makeText(requireContext(), "Video not found: " + videoName, Toast.LENGTH_SHORT).show();

    }

    private void playVideo(String actionName){
        if(actionName == null || actionName.isEmpty()){
            Log.e("Cloudinary", "No videos found!");
            return;
        }else {
            Intent intent = new Intent(requireContext(), VideoPlayer.class);
            intent.putExtra("actionName", actionName);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if(activity.getSupportActionBar() != null){
                activity.getSupportActionBar().show();
            }
        }
    }
}