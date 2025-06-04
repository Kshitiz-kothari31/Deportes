package com.example.deportes2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.List;


public class football_videos extends Fragment {

//    private String[] videoTexts = {
//            "Learning to pass a football accurately and effectively requires mastering technique, practicing regularly, and understanding the game. Here are the steps to improve your football passing skills: \n\n" +
//                    "1. Master the Basics of Holding the Ball\n\n" +
//                    "• Grip: Place your dominant hand's fingers on the laces, with your thumb underneath for support. Your pinky should wrap around the bottom of the ball.\n" +
//                    "• Support Hand: Use your non-dominant hand to stabilize the ball before throwing.\n\n" +
//                    "2. Learn the Proper Stance\n\n" +
//                    "• Feet Placement: Stand with your feet shoulder-width apart, with your non-dominant foot slightly forward.\n" +
//                    "• Weight Distribution: Keep your weight balanced and centered.\n\n" +
//                    "3. Practice the Throwing Motion\n\n" +
//                    "• Wind-Up: Hold the ball near your ear, with your elbow at a 90-degree angle.\n" +
//                    "• Step Forward: Step with your non-dominant foot toward your target as you begin the throwing motion.\n" +
//                    "• Release: Rotate your torso and extend your throwing arm, releasing the ball at eye level. Snap your wrist downward to create a spiral.\n" +
//                    "• Follow Through: Let your throwing hand naturally point toward your target after releasing the ball.\n\n" +
//                    "4. Focus on Accuracy\n\n" +
//                    "• Pick a Target: Always aim for a specific point (e.g., a receiver's chest or hands).\n" +
//                    "• Visualize: Imagine the ball traveling in a straight line to your target.\n\n" +
//                    "5. Work on the Spiral\n\n" +
//                    "• The spiral ensures the ball travels farther and with greater accuracy. To achieve it:\n" +
//                    "• Focus on snapping your wrist at the release.\n" +
//                    "• Ensure the ball rolls off your fingertips rather than being pushed forward.\n\n" +
//                    "6. Practice Different Types of Passes\n\n" +
//                    "• Short Passes: Use a quick release and minimal wind-up.\n" +
//                    "• Long Passes: Focus on generating power from your hips and legs.\n" +
//                    "• Screen Passes: Practice soft, controlled throws for close targets.\n\n" +
//                    "7. Incorporate Footwork Drills\n\n" +
//                    "• Practice sidestepping, backpedaling, and moving in the pocket while keeping your eyes on the target.\n" +
//                    "• Work on quick steps to set your stance after movement.\n\n" +
//                    "8. Work with a Partner or Target\n\n" +
//                    "• Start by throwing to a stationary partner or target.\n" +
//                    "• Progress to passing to a moving receiver to mimic game conditions.\n\n" +
//                    "9. Develop Game Awareness\n\n" +
//                    "• Practice reading defenses and making decisions under pressure.\n" +
//                    "• Learn to identify open receivers quickly.\n\n" +
//                    "10. Regularly Evaluate and Improve\n\n" +
//                    "• Record your throws to analyze your form and technique.\n" +
//                    "• Seek feedback from coaches or teammates.\n\n" +
//                    "11. Stay Consistent\n\n" +
//                    "• Dedicate time for regular practice, even if it’s just a few minutes a day.\n" +
//                    "• Gradually increase distance and intensity.\n\n",
//
//            "Dribbling is an essential football (soccer) skill that involves controlling the ball while moving around the field. Here’s a step-by-step guide to improve your dribbling skills:\n\n" +
//                    "1. Get Comfortable with the Ball\n\n" +
//                    "• Practice basic touches with the ball using different parts of your foot (inside, outside, sole, and laces).\n" +
//                    "• Try simple drills like tapping the ball back and forth between your feet to develop control.\n\n" +
//                    "2. Maintain Proper Body Position\n\n" +
//                    "• Knees slightly bent and body low for balance.\n" +
//                    "• Lean forward slightly and keep your weight on the balls of your feet for quick movements.\n" +
//                    "• Use your arms for balance.\n\n" +
//                    "3. Keep the Ball Close\n\n" +
//                    "• Touch the ball gently to keep it within a few feet of your body.\n" +
//                    "• Focus on small, controlled touches instead of kicking the ball too far ahead.\n\n" +
//                    "4. Use Different Parts of Your Foot\n\n" +
//                    "• Inside of the foot: For close control and short touches.\n" +
//                    "• Outside of the foot: For diagonal movement and quick changes of direction.\n" +
//                    "• Sole of the foot: To stop or drag the ball.\n\n" +
//                    "5. Look Up While Dribbling\n\n" +
//                    "• Develop the habit of glancing up regularly to scan the field and avoid obstacles.\n" +
//                    "• Practice dribbling without constantly staring at the ball.\n\n" +
//                    "6. Practice Basic Dribbling Drills\n\n" +
//                    "• Straight-line dribbling: Move the ball forward in a straight line using light touches.\n" +
//                    "• Figure 8s: Dribble around two cones in a figure-eight pattern.\n" +
//                    "• Cone drills: Set up cones in a zigzag and dribble through them.\n\n" +
//                    "7. Work on Speed and Control\n\n" +
//                    "• Start slow to master control, then gradually increase your speed.\n" +
//                    "• Focus on keeping the ball close even when running.\n\n" +
//                    "8. Learn to Change Direction Quickly\n\n" +
//                    "• Use techniques like step-overs, body feints, and the drag-back to outmaneuver opponents.\n" +
//                    "• Practice turning with the ball while maintaining control.\n\n" +
//                    "9. Improve with Game Scenarios\n\n" +
//                    "• Practice dribbling with a defender to simulate real-game situations.\n" +
//                    "• Work on shielding the ball by keeping your body between the defender and the ball.\n\n" +
//                    "10. Consistent Practice\n\n" +
//                    "• Dedicate time each day to practicing dribbling.\n" +
//                    "• Use both feet equally to become more versatile.\n\n" +
//                    "11. Study and Emulate\n\n" +
//                    "• Watch professional players like Lionel Messi or Neymar to observe their dribbling techniques.\n" +
//                    "• Try to replicate their movements during practice.\n\n" +
//                    "12. Play Small-Sided Games\n\n" +
//                    "• Join 1v1 or 2v2 matches to improve your dribbling in tight spaces.\n" +
//                    "• This will also help with decision-making and ball control under pressure.\n\n",
//
//            "Learning to shoot a football effectively requires a mix of technique, practice, and understanding of the game. Here’s a step-by-step guide:\n\n" +
//                    "Step 1: Warm-Up\n\n" +
//                    "• Dynamic Stretches: Loosen up your muscles with dynamic stretches like lunges, high knees, and leg swings.\n" +
//                    "• Light Jogging: Spend 5-10 minutes jogging to get your body warmed up.\n" +
//                    "• Ball Familiarity: Spend a few minutes dribbling or lightly passing the ball to improve control.\n\n" +
//                    "Step 2: Understand the Basics of Shooting\n\n" +
//                    "• Body Position:\n" +
//                    "   • Align your body toward the target (goal).\n" +
//                    "   • Keep your head up to aim, then down to focus on the ball as you shoot.\n\n" +
//                    "• Non-Kicking Foot:\n" +
//                    "   • Place your non-kicking foot beside the ball, slightly behind its center.\n" +
//                    "   • This foot provides balance and direction.\n\n" +
//                    "• Kicking Leg:\n" +
//                    "   • Swing your leg backward for power.\n" +
//                    "   • Strike the ball with the right part of your foot based on the desired shot type (e.g., instep for power, inside for accuracy, outside for curve).\n\n" +
//                    "Step 3: Practice Proper Technique\n\n" +
//                    "   1. Striking with the Instep (Laces):\n" +
//                    "      • Strike the ball with the top of your foot (laces area) for power.\n" +
//                    "      • Keep your toes pointing downward and lock your ankle.\n" +
//                    "      • Lean slightly forward to keep the shot low.\n\n" +
//                    "   2. Inside Foot Shot:\n\n" +
//                    "      • Use the inside of your foot for controlled and accurate shots.\n" +
//                    "      • Keep your ankle firm and swing naturally.\n" +
//                    "      • Aim for placement rather than power.\n\n" +
//                    "   3. Curved Shots (Bending):\n\n" +
//                    "      • Strike the ball slightly off-center with the inside or outside of your foot.\n" +
//                    "      • Follow through across your body to generate spin.\n\n" +
//                    "Step 4: Focus on the Follow-Through\n\n" +
//                    "• After striking the ball, follow through with your kicking leg.\n" +
//                    "• Your body should naturally move in the direction of the target.\n" +
//                    "• For a powerful shot, a full follow-through helps generate more force.\n\n" +
//                    "Step 5: Work on Aim and Accuracy\n\n" +
//                    "• Practice shooting at specific targets in the goal (e.g., top corners, bottom corners).\n" +
//                    "• Use cones or markers to simulate defenders or a narrower goal.\n\n" +
//                    "Step 6: Master Timing and Decision-Making\n\n" +
//                    "• Learn to shoot under pressure by simulating game situations.\n" +
//                    "• Work on quick decisions: when to shoot versus pass or dribble.\n" +
//                    "• Practice different types of shots based on scenarios (e.g., volleys, penalties, long-range shots).\n\n" +
//                    "Step 7: Train Regularly\n\n" +
//                    "   1.Incorporate shooting drills into your routine, such as:\n" +
//                    "      •  One-touch shooting: Take one touch to control and immediately shoot.\n" +
//                    "   2. Shooting after dribbling: Dribble past cones or defenders before shooting.\n" +
//                    "      • Partner drills: Have someone pass to you while you focus on shooting accuracy.\n\n" +
//                    "Step 8: Analyze and Adjust\n\n" +
//                    "• Record yourself or have someone watch your technique.\n" +
//                    "• Identify areas for improvement (e.g., power, accuracy, timing).\n" +
//                    "• Incorporate feedback into your training.\n\n" +
//                    "Step 9: Game Practice\n\n" +
//                    "• Play small-sided games to simulate real-match shooting opportunities.\n" +
//                    "• Focus on composure and positioning to create better shooting chances.\n\n" +
//                    "Step 10: Mental Preparation\n\n" +
//                    "• Build confidence by practicing regularly.\n" +
//                    "• Visualize successful shots before attempting them in games.\n\n" +
//                    "By consistently practicing and refining your technique, you’ll develop the ability to shoot effectively in any game situation.\n\n",
//
//            "Mastering football (soccer) ball control involves both basic techniques and advanced skills. Here’s a step-by-step guide to improve your ball control:\n\n" +
//                    "1. Foundation: Basic Touches\n\n" +
//                    "    1. Dribbling Practice:\n\n" +
//                    "       • Use the inside, outside, and sole of your foot to dribble the ball.\n" +
//                    "       • Keep the ball close, aiming for small, controlled touches.\n\n" +
//                    "    2. Toe Taps:\n\n" +
//                    "       • Stand with the ball stationary and tap it alternately with each foot.\n" +
//                    "       • Helps improve rhythm and coordination.\n\n" +
//                    "       • Inside-Outside Touch:\n\n" +
//                    "       • Alternate between touching the ball with the inside and outside of the same foot.\n\n" +
//                    "2. Ball Familiarity Drills\n\n" +
//                    "    1. Juggling:\n\n" +
//                    "       • Use your thighs, feet, and head to keep the ball in the air.\n" +
//                    "       • Focus on consistency rather than height or power.\n\n" +
//                    "    2. Rolls and Pullbacks:\n\n" +
//                    "       • Use the sole of your foot to roll the ball in different directions.\n" +
//                    "       • Practice pullbacks to stop and redirect the ball.\n\n" +
//                    "3. Receiving the Ball (First Touch)\n\n" +
//                    "    1. Ground Control:\n\n" +
//                    "       • Use the inside, outside, or sole of your foot to stop or redirect a ground pass.\n\n" +
//                    "    2. Aerial Control:\n\n" +
//                    "       • Use your chest, thighs, or feet to bring the ball under control from the air.\n" +
//                    "       • Focus on soft, cushioned touches to keep the ball close.\n\n" +
//                    "4. Direction and Movement\n\n" +
//                    "    1. Turning with the Ball:\n\n" +
//                    "       • Practice turns like the Cruyff Turn, Drag Back, or Step Over Turn.\n\n" +
//                    "    2. Shielding:\n\n" +
//                    "       • Use your body to protect the ball from opponents while maintaining control.\n\n" +
//                    "    3. Side-to-Side Movement:\n\n" +
//                    "       • Practice lateral dribbling to evade opponents.\n\n" +
//                    "5. Agility and Coordination Drills\n\n" +
//                    "    1. Cone Drills:\n\n" +
//                    "       • Set up cones and practice weaving through them using different parts of your foot.\n" +
//                    "    2. 1v1 Practice:\n\n" +
//                    "       • Engage in small-sided drills with a partner to simulate game scenarios.\n" +
//                    "    3. Speed Variation:\n\n" +
//                    "       • Alternate between slow and fast dribbling to mimic real-game situations.\n\n" +
//                    "6. Advance Your Skills\n\n" +
//                    "    1. Skill Moves:\n\n" +
//                    "       • Learn moves like step-overs, feints, and drag flicks.\n" +
//                    "       • Combine moves with directional changes for effective gameplay.\n" +
//                    "    2. Ball Manipulation:\n\n" +
//                    "       • Control the ball in tight spaces to mimic match pressure.\n\n" +
//                    "7. Game Situations\n\n" +
//                    "   1. Small-Sided Games:\n\n" +
//                    "      • Practice in environments that emphasize ball control, like futsal or 3v3.\n" +
//                    "   2. Match Play:\n\n" +
//                    "      • Apply your skills in full games, focusing on maintaining control under pressure.\n\n" +
//                    "8. Consistency and Improvement\n\n" +
//                    "   1. Daily Practice:\n\n" +
//                    "      • Dedicate at least 15–30 minutes daily to ball control drills.\n" +
//                    "   2. Feedback and Analysis:\n\n" +
//                    "      • Record your practice sessions to identify strengths and areas for improvement.\n" +
//                    "   3. Footwear Check:\n\n" +
//                    "      • Use comfortable, well-fitted cleats to improve touch.\n\n" +
//                    "By dedicating time to these steps and progressively challenging yourself, your ball control skills will improve significantly.\n\n",
//
//            "Learning to head a football correctly and safely requires proper technique, practice, and attention to safety. Here's a step-by-step guide:\n\n" +
//                    "1. Understand the Basics\n\n" +
//                    "   • Purpose of Heading: Used to pass, clear, or shoot the ball with your head.\n" +
//                    "   • Contact Point: Use the forehead (just above the eyebrows) for better control and safety.\n" +
//                    "   • Posture: Stand balanced with feet shoulder-width apart.\n\n" +
//                    "2. Warm-Up\n\n" +
//                    "   • Stretch Your Neck and Shoulders: Perform gentle stretches to prepare the muscles around your neck and shoulders.\n" +
//                    "   • Light Ball Work: Get comfortable with the ball using other parts of your body before starting headers.\n\n" +
//                    "3. Practice Proper Technique\n\n" +
//                    "   Body Position:\n\n" +
//                    "      • Stand with your knees slightly bent for balance.\n" +
//                    "      • Keep your eyes on the ball and maintain focus on its trajectory.\n\n" +
//                    "   Contact Point:\n\n" +
//                    "      • Meet the ball with the flat part of your forehead.\n" +
//                    "      • Avoid hitting with the top of your head or the sides.\n\n" +
//                    "   Timing:\n\n" +
//                    "      • Move towards the ball; don’t let it just hit you. Generate power by pushing forward with your neck and upper body.\n\n" +
//                    "   Neck Movement:\n\n" +
//                    "      • Use your neck muscles to push your head forward and add power.\n" +
//                    "      • Avoid stiffening your neck—stay relaxed but controlled.\n\n" +
//                    "4. Start with Controlled Drills\n\n" +
//                    "   Partner or Wall Practice:\n\n" +
//                    "      • Toss the ball lightly to yourself or have a partner throw it gently.\n" +
//                    "      • Practice heading it back with control.\n\n" +
//                    "   Ball-on-String Drill:\n\n" +
//                    "      • Suspend the ball on a string or rope. This allows repeated, controlled practice.\n\n" +
//                    "5. Practice Specific Types of Headers\n\n" +
//                    "   Defensive Header: Focus on distance, clearing the ball far from danger.\n" +
//                    "   Attacking Header: Practice aiming for the goal or a teammate.\n" +
//                    "   Lob or Flick Header: Use softer touches to redirect the ball.\n\n" +
//                    "6. Increase Difficulty Gradually\n\n" +
//                    "   • Start with soft throws or stationary balls.\n" +
//                    "   • Progress to practicing with moving balls (e.g., crosses or corner kicks).\n\n" +
//                    "7. Focus on Safety\n\n" +
//                    "   Avoid overtraining: take breaks to prevent neck strain.\n\n" +
//                    "      • Be mindful of the ball's weight and air pressure—practice with a properly inflated ball.\n" +
//                    "      • If heading during a game, ensure proper awareness of other players to avoid collisions.\n\n" +
//                    "8. Strengthen Supporting Muscles\n\n" +
//                    "   • Include neck, shoulder, and core exercises in your training.\n" +
//                    "   • This enhances power and minimizes the risk of injury.\n\n" +
//                    "9. Analyze and Adjust\n\n" +
//                    "   • Record your practice sessions to observe and correct your technique.\n" +
//                    "   • Seek feedback from a coach or experienced player.\n\n" +
//                    "10. Practice Regularly\n\n" +
//                    "   • Repetition is key to developing confidence and accuracy.\n" +
//                    "   • Set aside dedicated time each week to focus on headers.\n\n",
//
//            "Chest control is an essential skill in football (soccer) for effectively receiving aerial balls and maintaining possession. Here are the steps to improve your chest control:\n\n" +
//                    "1. Understand the Basics\n\n" +
//                    "   • The goal is to absorb the ball's impact and direct it where you want, either to drop at your feet or redirect to a teammate.\n" +
//                    "   • The chest should act as a cushion, not a hard surface.\n\n" +
//                    "2. Position Yourself\n\n" +
//                    "   • Face the Ball: Always position your body to meet the ball head-on for better control.\n" +
//                    "   • Balance and Stance: Stand with your feet shoulder-width apart and knees slightly bent for stability.\n\n" +
//                    "3. Prepare Your Chest\n\n" +
//                    "   • Expand Your Chest: Push your chest slightly outward to provide a solid but cushioned surface.\n" +
//                    "   • Relax Your Body: Tension can cause the ball to bounce unpredictably. Stay relaxed and ready to adjust.\n\n" +
//                    "4. Time Your Movement\n\n" +
//                    "   • Read the Ball's Trajectory: Judge where and how fast the ball is coming.\n" +
//                    "   • Step Into the Path: Move forward slightly to meet the ball, reducing its momentum.\n\n" +
//                    "5. Absorb the Impact\n\n" +
//                    "   • Lean back slightly as the ball makes contact with your chest. This helps cushion the ball and control its drop.\n\n" +
//                    "6. Direct the Ball\n\n" +
//                    "   • Use the upper part of your chest (just below the collarbone) for better precision.\n" +
//                    "   • Adjust your chest angle to guide the ball to your feet or towards a specific direction.\n\n" +
//                    "7. Practice Drills\n\n" +
//                    "Solo Practice:\n" +
//                    "\n" +
//                    "   • Toss the ball up and control it with your chest, ensuring it lands at your feet.\n" +
//                    "   • Vary the height and speed of your tosses.\n" +
//                    "   • Partner Practice:\n" +
//                    "\n" +
//                    "   • Have a partner toss or kick the ball to you at different angles and speeds.\n" +
//                    "   • Focus on maintaining control and returning the ball with accuracy.\n" +
//                    "Wall Drill:\n" +
//                    "\n" +
//                    "   • Kick the ball against a wall, let it bounce back, and control it with your chest.\n" +
//                    "   • Increase the difficulty by standing further back or kicking harder.\n" +
//                    "   • Game-Like Scenarios:\n" +
//                    "\n" +
//                    "• During small-sided games or scrimmages, actively seek to use your chest to control aerial passes.\n\n" +
//                    "8. Refine Your Technique\n\n" +
//                    "   • Video Analysis: Record your practice to analyze and improve your technique.\n" +
//                    "   • Feedback: Ask a coach or experienced player for tips on improving your form.\n\n" +
//                    "9. Improve Fitness and Strength\n\n" +
//                    "   • Core Strength: A strong core enhances stability and balance during chest control.\n" +
//                    "   • Coordination: Drills like juggling improve your overall ball control and reaction time.\n\n" +
//                    "10. Stay Consistent\n\n" +
//                    "   • Make chest control a regular part of your training routine.\n" +
//                    "   • Progressively challenge yourself with faster and more unpredictable passes.\n\n",
//
//    };


//    private String extractPublicId(String url) {
//        String[] parts = url.split("/upload/");
//        if (parts.length > 1) {
//            return parts[1].split("\\.")[0]; // Removes .mp4 or other extensions
//        } else {
//            Log.e("Debug", "Invalid Cloudinary URL format!");
//            return null;
//        }
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_football_videos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton f_passing, f_dribbling, f_shooting,  f_controlling, f_header, f_chest_control;

        f_passing = view.findViewById(R.id.football_passing);
        f_passing.setOnClickListener(new View.OnClickListener() {
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

        f_dribbling = getView().findViewById(R.id.football_Dribbling);
        f_dribbling.setOnClickListener(new View.OnClickListener() {
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

        f_shooting = getView().findViewById(R.id.football_Shooting);
        f_shooting.setOnClickListener(new View.OnClickListener() {
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

        f_controlling = getView().findViewById(R.id.football_Controlling);
        f_controlling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("controlling.mp4", videos);
            }
        });

        f_header = getView().findViewById(R.id.football_Header);
        f_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("header.mp4", videos);
            }
        });

        f_chest_control = getView().findViewById(R.id.football_Chest_Controlling);
        f_chest_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> videos = MainActivity.videoPublicIds;
                if (videos == null || videos.isEmpty()) {
                    Toast.makeText(requireContext(), "No videos available yet! Try again in a second.", Toast.LENGTH_SHORT).show();
                    Log.e("Debug", "videoPublicIds is NULL or EMPTY when clicking the button!");
                    return;
                }
                playVideoWithName("chest_control.mp4", videos);
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